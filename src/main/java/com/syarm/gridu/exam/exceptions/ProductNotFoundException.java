package com.syarm.gridu.exam.exceptions;

public class ProductNotFoundException extends ApplicationException {
    public ProductNotFoundException(Long productId) {
        super("Product with id " + productId + " not found");
    }
}
