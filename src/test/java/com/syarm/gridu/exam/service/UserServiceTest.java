package com.syarm.gridu.exam.service;

import com.syarm.gridu.exam.exceptions.OrderNotFoundException;
import com.syarm.gridu.exam.exceptions.ProductNotFoundException;
import com.syarm.gridu.exam.model.Order;
import com.syarm.gridu.exam.model.OrderItem;
import com.syarm.gridu.exam.model.Product;
import com.syarm.gridu.exam.model.User;
import com.syarm.gridu.exam.model.dto.UserOrder;
import com.syarm.gridu.exam.repository.OrderUserMappingRepository;
import com.syarm.gridu.exam.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private OrderUserMappingRepository orderUserMappingRepository;
    @MockBean
    private OrderService orderService;
    @MockBean
    private ProductService productService;

    @Autowired
    private UserService underTest;

    @Test
    public void getUserById_userNotFound() {
        StepVerifier.create(underTest.getUserById(null))
                .expectNextCount(0)
                .verifyComplete();

        verify(orderUserMappingRepository, never()).getOrdersByUserIdFlux(any(Long.class));
    }

    @Test
    public void getUserById_userNotFound2() {
        StepVerifier.create(underTest.getUserById(10L))
                .expectNextCount(0)
                .verifyComplete();

        verify(orderUserMappingRepository, never()).getOrdersByUserIdFlux(any(Long.class));
    }

    @Test
    public void getUserById_noOrders() {
        long userId = 0L;
        User user = mock(User.class);
        when(userRepository.getUserByIdMono(userId)).thenReturn(Mono.just(user));

        when(orderUserMappingRepository.getOrdersByUserIdFlux(userId))
                .thenReturn(Flux.empty());

        StepVerifier.create(underTest.getUserById(userId))
                .expectNextCount(0)
                .verifyComplete();

        verify(orderService, never()).findOrderByIdMono(userId);
        verify(productService, never()).findProductByIdMono(userId);
    }

    @Test
    public void getUserById_orderNotFound() {
        long userId = 0L;
        User user = mock(User.class);
        when(userRepository.getUserByIdMono(userId)).thenReturn(Mono.just(user));

        long orderId = 1L;

        when(orderUserMappingRepository.getOrdersByUserIdFlux(userId))
                .thenReturn(Flux.fromIterable(List.of(orderId)));

        when(orderService.findOrderByIdMono(orderId)).thenReturn(Mono.error(new OrderNotFoundException(orderId)));

        StepVerifier.create(underTest.getUserById(userId))
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    public void getUserById_orderEmpty() {
        long userId = 0L;
        User user = new User(userId, "User #" + userId);
        when(userRepository.getUserByIdMono(userId)).thenReturn(Mono.just(user));

        long orderId = 1L;

        when(orderUserMappingRepository.getOrdersByUserIdFlux(userId))
                .thenReturn(Flux.fromIterable(List.of(orderId)));

        Order order = new Order(
                orderId,
                System.currentTimeMillis(),
                List.of()
        );
        when(orderService.findOrderByIdMono(orderId)).thenReturn(Mono.just(order));

        StepVerifier.create(underTest.getUserById(userId))
                .expectNextCount(0)
                .verifyComplete();

        verify(productService, never()).findProductByIdMono(anyLong());
    }

    @Test
    public void getUserById_productNotFound() {
        long userId = 0L;
        User user = new User(userId, "User #" + userId);
        when(userRepository.getUserByIdMono(userId)).thenReturn(Mono.just(user));

        long orderId = 1L;

        when(orderUserMappingRepository.getOrdersByUserIdFlux(userId))
                .thenReturn(Flux.fromIterable(List.of(orderId)));

        long productId = 3L;
        when(productService.findProductByIdMono(productId))
                .thenReturn(Mono.error(new ProductNotFoundException(productId)));

        long orderItemId = 2L;
        OrderItem item = new OrderItem(
                orderItemId,
                productId,
                null,
                100,
                123.4
        );

        Order order = new Order(
                orderId,
                System.currentTimeMillis(),
                List.of(item)
        );
        when(orderService.findOrderByIdMono(orderId)).thenReturn(Mono.just(order));

        UserOrder expected = new UserOrder(
                user.getId(),
                user.getName(),
                order.getId(),
                order.getDate(),
                List.of(item)
        );

        StepVerifier.create(underTest.getUserById(userId))
                .expectNextMatches((expected::equals))
                .verifyComplete();
    }

    @Test
    public void getUserById_success() {
        long userId = 0L;
        User user = new User(userId, "User #" + userId);
        when(userRepository.getUserByIdMono(userId)).thenReturn(Mono.just(user));

        long orderId = 1L;

        when(orderUserMappingRepository.getOrdersByUserIdFlux(userId))
                .thenReturn(Flux.fromIterable(List.of(orderId)));

        long productId = 3L;
        Product product = new Product(productId, "Product #" + productId);
        when(productService.findProductByIdMono(productId))
                .thenReturn(Mono.just(product));

        long orderItemId = 2L;
        OrderItem item = new OrderItem(
                orderItemId,
                productId,
                null,
                100,
                123.4
        );

        Order order = new Order(
                orderId,
                System.currentTimeMillis(),
                List.of(item)
        );
        when(orderService.findOrderByIdMono(orderId)).thenReturn(Mono.just(order));

        UserOrder expected = new UserOrder(
                user.getId(),
                user.getName(),
                order.getId(),
                order.getDate(),
                List.of(item)
        );

        StepVerifier.create(underTest.getUserById(userId))
                .expectNextMatches((expected::equals))
                .verifyComplete();
    }
}
