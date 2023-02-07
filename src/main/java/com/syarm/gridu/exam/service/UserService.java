package com.syarm.gridu.exam.service;

import com.syarm.gridu.exam.model.dto.UserInfoDTO;
import com.syarm.gridu.exam.repository.UserRepository;
import com.syarm.gridu.exam.service.web.OrderWebClientService;
import com.syarm.gridu.exam.service.web.ProductWebClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final OrderWebClientService orderService;
    private final ProductWebClientService productService;

    public Flux<UserInfoDTO> getUserInfoById(String userId, String requestId) {
        return userRepository.findById(userId)
                .flatMapMany(user -> orderService.findOrdersByPhoneNumberFlux(user.getPhone(), requestId)
                        .map(orderDto -> {
                            var userInfoDto = new UserInfoDTO();
                            userInfoDto.setUserName(user.getName());
                            userInfoDto.setPhoneNumber(user.getPhone());
                            userInfoDto.setOrderNumber(orderDto.getOrderNumber());
                            userInfoDto.setProductCode(orderDto.getProductCode());

                            return userInfoDto;
                        }))
                .flatMap(userInfoDto ->
                    productService.findProductsByCodeFlux(userInfoDto.getProductCode(), requestId)
                            .onErrorResume(e -> Flux.empty())
                            .reduce((p1, p2) -> p1.getScore() > p2.getScore() ? p1 : p2)
                            .map(productDto -> {
                                userInfoDto.setProductId(productDto.getProductId());
                                userInfoDto.setProductName(productDto.getProductName());

                                return userInfoDto;
                            })
                            .switchIfEmpty(Mono.just(userInfoDto))
                );
    }
}
