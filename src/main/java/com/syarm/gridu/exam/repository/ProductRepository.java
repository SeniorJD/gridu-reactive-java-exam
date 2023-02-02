package com.syarm.gridu.exam.repository;

import com.syarm.gridu.exam.model.Product;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ProductRepository {
    public static final int PRODUCTS_COUNT = 1000;

    private final Map<Long, Product> productMap = new HashMap<>();

    public Product getProductById(long productId) {
        return productMap.get(productId);
    }

    @PostConstruct
    public void fillMap() {
        long id = 1L;

        for (long i = 0; i < PRODUCTS_COUNT; i++) {
            productMap.put(i, generateProduct(i));
        }
    }

    private Product generateProduct(long id) {
        return new Product(
                id,
                "Product #" + id
        );
    }
}
