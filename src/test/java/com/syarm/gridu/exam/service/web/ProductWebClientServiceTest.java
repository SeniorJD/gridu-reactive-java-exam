package com.syarm.gridu.exam.service.web;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import com.syarm.gridu.exam.model.dto.ProductDTO;
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
public class ProductWebClientServiceTest {
    private static final String REQUEST_ID = "REQUEST_ID";
    private static final String PRODUCT_CODE = "PRODUCT_CODE";

    @Autowired
    private ProductWebClientService underTest;

    private WireMockServer server;

    @BeforeEach
    void before() {
        this.server = new WireMockServer(wireMockConfig().port(8084));

        this.server.start();
    }

    @AfterEach
    void after() {
        this.server.stop();
    }

    @Test
    void findProductsByCodeFluxTest() {
        ProductDTO expected = new ProductDTO();
        expected.setProductId("productId");
        expected.setProductName("productName");
        expected.setProductCode("productCode");
        expected.setScore(123.45);

        var expectedJson = new GsonJsonProvider().toJson(expected);

        this.server.stubFor(get("/productInfoService/product/names?productCode=" + PRODUCT_CODE).willReturn(
                aResponse()
                        .withStatus(200)
                        .withBody(expectedJson)
                        .withHeader("Content-type", MediaType.APPLICATION_NDJSON_VALUE)
        ));

        Flux<ProductDTO> flux = this.underTest.findProductsByCodeFlux(PRODUCT_CODE, REQUEST_ID);

        StepVerifier.create(flux)
                .assertNext(actual -> {
                    Assertions.assertEquals(expected.getProductId(), actual.getProductId());
                    Assertions.assertEquals(expected.getProductName(), actual.getProductName());
                    Assertions.assertEquals(expected.getProductCode(), actual.getProductCode());
                    Assertions.assertEquals(expected.getScore(), actual.getScore());
                })
                .verifyComplete();
    }
}
