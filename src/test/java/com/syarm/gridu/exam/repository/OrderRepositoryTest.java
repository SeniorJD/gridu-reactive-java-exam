package com.syarm.gridu.exam.repository;

import com.syarm.gridu.exam.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.syarm.gridu.exam.repository.OrderRepository.ORDERS_COUNT;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class OrderRepositoryTest {

    private OrderRepository underTest;

    @BeforeEach
    public void setUp() {
        underTest = new OrderRepository();
        underTest.fillMap();
    }

    @Test
    public void findByIdTest() {
        Order byId = underTest.findById(ORDERS_COUNT - 1);

        assertNotNull(byId);
    }

    @Test
    public void findByIdTest_none() {
        Order byId = underTest.findById(ORDERS_COUNT);

        assertNull(byId);
    }
}
