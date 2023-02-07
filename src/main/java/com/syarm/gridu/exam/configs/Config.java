package com.syarm.gridu.exam.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class Config {

    @Value("${service.product.baseUrl}")
    String productServiceBaseUrl;

    @Value("${service.product.findProductByIdPath}")
    String findProductByIdPath;

    @Value("${service.order.baseUrl}")
    String orderServiceBaseUrl;

    @Value("${service.order.findOrderByOrderIdPath}")
    String orderServiceUri;

    @Value("${db.username}")
    private String dbUsername;
    @Value("${db.name}")
    private String dbName;
    @Value("${db.password}")
    private String dbPassword;

    @Bean("findProductByCodePath")
    public String productServiceUri() {
        return findProductByIdPath;
    }

    @Bean("orderServiceUri")
    public String orderServiceUri() {
        return orderServiceUri;
    }

    @Bean("productServiceWebClient")
    public WebClient productServiceWebClient() {
        return WebClient.builder()
                .baseUrl(productServiceBaseUrl)
                .build();
    }

    @Bean("orderServiceWebClient")
    public WebClient orderServiceWebClient() {
        return WebClient.builder()
                .baseUrl(orderServiceBaseUrl)
                .build();
    }

    @Bean("dbUsername")
    public String dbUsername() {
        return dbUsername;
    }

    @Bean("dbName")
    public String dbName() {
        return dbName;
    }

    @Bean("dbPassword")
    public String dbPassword() {
        return dbPassword;
    }
}
