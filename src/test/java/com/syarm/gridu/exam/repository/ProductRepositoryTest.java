package com.syarm.gridu.exam.repository;

import com.syarm.gridu.exam.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class ProductRepositoryTest {
    private ProductRepository productRepository;

    @BeforeEach
    public void setUp() {
        productRepository = new ProductRepository();
        productRepository.fillMap();
    }

    @Test
    public void getProductById() {
        Product productById = productRepository.getProductById(0L);
        assertNotNull(productById);
    }

    @Test
    public void getProductById_null() {
        Product productById = productRepository.getProductById(ProductRepository.PRODUCTS_COUNT);
        assertNull(productById);
    }
}
