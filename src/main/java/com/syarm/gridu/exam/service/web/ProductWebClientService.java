package com.syarm.gridu.exam.service.web;

import com.syarm.gridu.exam.model.dto.ProductDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.util.context.Context;

import static com.syarm.gridu.exam.util.MDSLoggerUtil.logOnError;
import static com.syarm.gridu.exam.util.MDSLoggerUtil.logOnNext;

@Service
public class ProductWebClientService {
    private static final String PRODUCT_CODE_PARAM = "productCode";
    private static final String LOGGER_KEY = "productWebServiceLog";

    private static final Logger log = LoggerFactory.getLogger(ProductWebClientService.class);

    private final WebClient webClient;
    private final String findProductByCodePath;

    @Autowired
    public ProductWebClientService(@Qualifier("productServiceWebClient") WebClient webClient,
                                   @Qualifier("findProductByCodePath") String findProductByCodePath) {
        this.webClient = webClient;
        this.findProductByCodePath = findProductByCodePath;
    }

    public Flux<ProductDTO> findProductsByCodeFlux(String productCode, String requestId) {
        return webClient.get().uri(uriBuilder ->
                        uriBuilder.path(findProductByCodePath)
                            .queryParam(PRODUCT_CODE_PARAM, productCode)
                            .build())
                .retrieve()
                .bodyToFlux(ProductDTO.class)
                .doOnEach(logOnNext(
                        LOGGER_KEY,
                        orderDto -> log.info("{}: Find Products. Response: {}", requestId, orderDto)))
                .doOnEach(logOnError(
                        LOGGER_KEY,
                        e -> log.error("{}: Failed to find products: {}", requestId, e)))
                .contextWrite(Context.of(LOGGER_KEY, requestId));
    }
}
