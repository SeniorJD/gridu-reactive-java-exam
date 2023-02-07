package com.syarm.gridu.exam.service.web;

import com.syarm.gridu.exam.model.dto.OrderDTO;
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
public class OrderWebClientService {
    private static final String PHONE_NUMBER_PARAM = "phoneNumber";
    private static final String LOGGER_KEY = "orderWebClientLog";

    private static final Logger log = LoggerFactory.getLogger(OrderWebClientService.class);

    private final WebClient webClient;
    private final String findOrderByPhonePath;

    @Autowired
    public OrderWebClientService(@Qualifier("orderServiceWebClient") WebClient webClient,
                                 @Qualifier("orderServiceUri") String findOrderByPhonePath
    ) {
        this.webClient = webClient;
        this.findOrderByPhonePath = findOrderByPhonePath;
    }

    public Flux<OrderDTO> findOrdersByPhoneNumberFlux(String phoneNumber, String requestId) {
        return webClient.get().uri(
                        uriBuilder -> uriBuilder.path(findOrderByPhonePath)
                                .queryParam(PHONE_NUMBER_PARAM, phoneNumber)
                                .build())
                .retrieve()
                .bodyToFlux(OrderDTO.class)
                .doOnEach(logOnNext(
                        LOGGER_KEY,
                        orderDto -> log.info("{}: Find orders. Response: {}", requestId, orderDto)))
                .doOnEach(logOnError(
                        LOGGER_KEY,
                        e -> log.error("{}: Failed to find orders: {}", requestId, e)))
                .contextWrite(Context.of(LOGGER_KEY, requestId));
    }
}
