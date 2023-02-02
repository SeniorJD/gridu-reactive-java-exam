package com.syarm.gridu.exam.service;

import com.syarm.gridu.exam.exceptions.ProductNotFoundException;
import com.syarm.gridu.exam.model.Product;
import com.syarm.gridu.exam.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductServiceTest {

    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private ProductService underTest;

    @Test
    public void findProductById_null() {
        Product productById = underTest.findProductById(null);
        assertNull(productById);
    }

    @Test
    public void findProductById() {
        long id = 0L;
        Product product = mock(Product.class);
        when(productRepository.getProductById(id)).thenReturn(product);
        Product productById = underTest.findProductById(id);

        verify(productRepository).getProductById(id);
        assertEquals(product, productById);
    }

    @Test
    public void findProductByIdMono_null() {
        StepVerifier.create(underTest.findProductByIdMono(null))
                .expectError(ProductNotFoundException.class)
                .verify();
    }

    @Test
    public void findProductByIdMono() {
        long id = 0L;
        Product product = mock(Product.class);
        when(productRepository.getProductById(id)).thenReturn(product);
        StepVerifier.create(underTest.findProductByIdMono(id))
                        .expectNext(product)
                        .verifyComplete();
    }
}
