package com.syarm.gridu.exam.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class OrderUserMappingRepositoryTest {
    private OrderUserMappingRepository underTest;

    @BeforeEach
    public void setUp() {
        underTest = new OrderUserMappingRepository();
        underTest.fillMap();
    }

    @Test
    public void getOrdersByUserId() {
        for (int i = 0; i <= UserRepository.USERS_COUNT; i++) {
            List<Long> ordersByUserId = underTest.getOrdersByUserId(i);
            assertEquals(Math.max(0, UserRepository.USERS_COUNT - 1 - i), ordersByUserId.size());
        }
    }

    @Test
    public void getOrdersByUserIdFlux() {
        for (int i = 0; i <= UserRepository.USERS_COUNT; i++) {
            StepVerifier.create(underTest.getOrdersByUserIdFlux(i))
                    .expectNextCount(Math.max(0, UserRepository.USERS_COUNT - 1 - i))
                    .verifyComplete();
        }
    }
}
