package com.syarm.gridu.exam.service;

import com.syarm.gridu.exam.exceptions.ProductNotFoundException;
import com.syarm.gridu.exam.model.Product;
import com.syarm.gridu.exam.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product findProductById(Long productId) {
        if (productId == null) {
            return null;
        }
        return productRepository.getProductById(productId);
    }

    public Mono<Product> findProductByIdMono(Long productId) {
        if (productId == null) {
            return Mono.error(new ProductNotFoundException(productId));
        }

        return Mono.justOrEmpty(productRepository.getProductById(productId))
                .switchIfEmpty(Mono.error(new ProductNotFoundException(productId)));
    }
}
