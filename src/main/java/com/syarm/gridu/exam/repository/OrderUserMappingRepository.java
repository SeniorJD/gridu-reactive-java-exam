package com.syarm.gridu.exam.repository;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class OrderUserMappingRepository {
    private final Map<Long, Long> orderUserMappingMap = new HashMap<>();

    public List<Long> getOrdersByUserId(long userId) {
        return orderUserMappingMap.entrySet().stream()
                .filter(entry -> entry.getValue().equals(userId))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public Flux<Long> getOrdersByUserIdFlux(long userId) {
        return Flux.fromIterable(getOrdersByUserId(userId));
    }

    @PostConstruct
    public void fillMap() {
        long orderId = OrderRepository.ORDERS_COUNT - 1;
        for (long userId = UserRepository.USERS_COUNT - 1; userId >= 0; userId--) {
            for (long i = userId; i < UserRepository.USERS_COUNT - 1; i++) {
                orderUserMappingMap.put(orderId--, userId);
            }
        }
    }
}
