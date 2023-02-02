package com.syarm.gridu.exam.service;

import com.syarm.gridu.exam.exceptions.UserNotFoundException;
import com.syarm.gridu.exam.model.Order;
import com.syarm.gridu.exam.model.User;
import com.syarm.gridu.exam.model.dto.UserOrder;
import com.syarm.gridu.exam.repository.OrderUserMappingRepository;
import com.syarm.gridu.exam.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final OrderUserMappingRepository orderUserMappingRepository;
    private final OrderService orderService;
    private final ProductService productService;

    public Flux<UserOrder> getUserById(Long userId) {
        return findUserById(userId)
                .flatMapMany(user ->
                    orderUserMappingRepository.getOrdersByUserIdFlux(userId)
                        .flatMap(orderService::findOrderByIdMono)
                        .map(order -> convert(user, order))
                        .log()
                )
                .filter(userOrder -> userOrder.getOrderItems().size() > 0)
                .doOnNext(this::fillProductInfo)
                .log()
                .onErrorResume(e -> Flux.empty());
    }

    private UserOrder convert(User user, Order order) {
        var userOrder = new UserOrder();
        userOrder.setUserId(user.getId());
        userOrder.setUserName(user.getName());
        userOrder.setOrderId(order.getId());
        userOrder.setOrderDate(order.getDate());
        userOrder.setOrderItems(order.getItems());
        return userOrder;
    }

    private void fillProductInfo(UserOrder order) {
        Flux.fromIterable(order.getOrderItems())
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(orderItem ->
                    productService.findProductByIdMono(orderItem.getProductId())
                        .onErrorComplete()
                        .subscribe(product -> orderItem.setProductName(product.getProductName())))
                .log()
                .subscribe();
    }

    private Mono<User> findUserById(Long userId) {
        return Mono.justOrEmpty(userId)
                .switchIfEmpty(Mono.error(new UserNotFoundException(userId)))
                .flatMap(userRepository::getUserByIdMono)
                .switchIfEmpty(Mono.error(new UserNotFoundException(userId)));
    }
}
