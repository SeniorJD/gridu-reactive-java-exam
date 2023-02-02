package com.syarm.gridu.exam.service;

import com.syarm.gridu.exam.exceptions.OrderNotFoundException;
import com.syarm.gridu.exam.model.Order;
import com.syarm.gridu.exam.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderServiceTest {
    @MockBean
    private OrderRepository orderRepository;

    @Autowired
    private OrderService underTest;

    @Test
    public void findOrderById_null() {
        Order orderById = underTest.findOrderById(null);
        assertNull(orderById);
    }

    @Test
    public void findOrderById() {
        long id = 0L;
        Order order = mock(Order.class);
        when(orderRepository.findById(id)).thenReturn(order);

        Order orderById = underTest.findOrderById(id);
        verify(orderRepository).findById(id);
        assertEquals(order, orderById);
    }

    @Test
    public void findOrderByIdMono_null() {
        StepVerifier.create(underTest.findOrderByIdMono(null))
                .expectError(OrderNotFoundException.class)
                .verify();
    }

    @Test
    public void findOrderByIdMono() {
        long id = 0L;
        Order order = mock(Order.class);
        when(orderRepository.findById(id)).thenReturn(order);

        StepVerifier.create(underTest.findOrderByIdMono(id))
                .expectNext(order)
                .verifyComplete();
    }
}
