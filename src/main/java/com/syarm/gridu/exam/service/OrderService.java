package com.syarm.gridu.exam.service;

import com.syarm.gridu.exam.exceptions.OrderNotFoundException;
import com.syarm.gridu.exam.model.Order;
import com.syarm.gridu.exam.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order findOrderById(Long orderId) {
        if (orderId == null) {
            return null;
        }
        return orderRepository.findById(orderId);
    }

    public Mono<Order> findOrderByIdMono(Long orderId) {
        if (orderId == null) {
            return Mono.error(new OrderNotFoundException(orderId));
        }

        return Mono.justOrEmpty(orderRepository.findById(orderId))
                .switchIfEmpty(Mono.error(new OrderNotFoundException(orderId)));
    }
}
