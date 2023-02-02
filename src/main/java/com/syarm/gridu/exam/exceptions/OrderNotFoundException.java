package com.syarm.gridu.exam.exceptions;

public class OrderNotFoundException extends ApplicationException {
    public OrderNotFoundException(Long orderId) {
        super("Order with id " + orderId + " not found");
    }
}
