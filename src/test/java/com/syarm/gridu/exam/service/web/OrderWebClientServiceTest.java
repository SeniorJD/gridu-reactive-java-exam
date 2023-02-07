package com.syarm.gridu.exam.service.web;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import com.syarm.gridu.exam.model.dto.OrderDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest
public class OrderWebClientServiceTest {
    private static final String REQUEST_ID = "REQUEST_ID";
    private static final String PHONE_NUMBER = "PHONE_NUMBER";

    @Autowired
    private OrderWebClientService underTest;

    private WireMockServer server;

    @BeforeEach
    void before() {
        this.server = new WireMockServer(wireMockConfig().port(8083));
        this.server.start();
    }

    @AfterEach
    void after() {
        this.server.stop();
    }

    @Test
    void findOrdersByPhoneNumberFluxTest() {
        OrderDTO expected = new OrderDTO();
        expected.setOrderNumber("orderNumber");
        expected.setPhoneNumber("phoneNumber");
        expected.setProductCode("productCode");

        var expectedJson = new GsonJsonProvider().toJson(expected);

        this.server.stubFor(get("/orderSearchService/order/phone?phoneNumber=" + PHONE_NUMBER).willReturn(
                aResponse()
                        .withStatus(200)
                        .withBody(expectedJson)
                        .withHeader("Content-type", MediaType.APPLICATION_NDJSON_VALUE)
        ));

        Flux<OrderDTO> flux = this.underTest.findOrdersByPhoneNumberFlux(PHONE_NUMBER, REQUEST_ID);

        StepVerifier.create(flux)
                .assertNext(actual -> {
                    Assertions.assertEquals(expected.getOrderNumber(), actual.getOrderNumber());
                    Assertions.assertEquals(expected.getPhoneNumber(), actual.getPhoneNumber());
                    Assertions.assertEquals(expected.getProductCode(), actual.getProductCode());
                })
                .verifyComplete();
    }

    @Test
    void findOrdersByPhoneNumberFlux_Error_500() {
        this.server.stubFor(get("/orderSearchService/order/phone?phoneNumber=" + PHONE_NUMBER).willReturn(
                aResponse()
                        .withStatus(500)
                        .withHeader("Content-type", MediaType.APPLICATION_NDJSON_VALUE)
        ));

        Flux<OrderDTO> flux = this.underTest.findOrdersByPhoneNumberFlux(PHONE_NUMBER, REQUEST_ID);

        StepVerifier.create(flux)
                .expectError()
                .verify();
    }
}
