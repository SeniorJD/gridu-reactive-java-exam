package com.syarm.gridu.exam.controller;

import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import com.syarm.gridu.exam.model.dto.UserInfoDTO;
import com.syarm.gridu.exam.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

@WebFluxTest(UserController.class)
public class UserControllerWebFluxTest {

    @MockBean
    private UserService userService;

    @Autowired
    private WebTestClient client;

    @Test
    public void getUserInfoByIdTest_2xx() {
        UserInfoDTO info = new UserInfoDTO();
        info.setProductName("productName");
        info.setUserName("userName");
        info.setProductId("productId");
        info.setProductCode("productCode");
        info.setPhoneNumber("phoneNumber");
        info.setOrderNumber("orderNumber");

        var requestId = UUID.randomUUID().toString();
        var userId = "123";

        Mockito.when(userService.getUserInfoById(userId, requestId))
                .thenReturn(Flux.just(info));

        var exchange = this.client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/user/" + userId)
                        .build())
                .header("REQUEST_ID", requestId)
                .exchange();

        var expectedJson = new GsonJsonProvider().toJson(List.of(info));

        exchange.expectStatus().is2xxSuccessful()
                .expectBody().json(expectedJson);
    }

    @Test
    public void getUserInfoByIdTest_4xx() {
        var userId = "123";
        var exchange = this.client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/user/" + userId)
                        .build())
                .exchange();

        exchange.expectStatus().is4xxClientError();
    }

    @Test
    public void getUserInfoByIdTest_5xx() {
        var userId = "123";
        var exchange = this.client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/user/" + userId)
                        .build())
                .header("REQUEST_ID", "")
                .exchange();

        exchange.expectStatus().is5xxServerError();
    }
}
