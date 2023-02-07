package com.syarm.gridu.exam.service;

import com.syarm.gridu.exam.model.User;
import com.syarm.gridu.exam.model.dto.OrderDTO;
import com.syarm.gridu.exam.model.dto.ProductDTO;
import com.syarm.gridu.exam.model.dto.UserInfoDTO;
import com.syarm.gridu.exam.repository.UserRepository;
import com.syarm.gridu.exam.service.web.OrderWebClientService;
import com.syarm.gridu.exam.service.web.ProductWebClientService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private static final String REQUEST_ID = "REQUEST_ID";
    private static final String USER_ID = "USER_ID";
    private static final String USER_NAME = "USER_NAME";
    private static final String PHONE_NUMBER = "PHONE_NUMBER";
    private static final String PRODUCT_CODE = "PRODUCT_CODE";
    private static final String PRODUCT_NAME = "PRODUCT_NAME";
    private static final String ORDER_NUMBER = "ORDER_NUMBER";

    private UserService underTest;

    @Mock
    private UserRepository userRepository;
    @Mock
    private OrderWebClientService orderWebClientService;
    @Mock
    private ProductWebClientService productWebClientService;

    @BeforeEach
    void setUp() {
        underTest = new UserService(
                userRepository,
                orderWebClientService,
                productWebClientService
        );
    }

    @Test
    void getUserInfoByIdTest() {
        var user = generateUser();
        var orderDto = generateOrder();
        var productDto = generateProduct("productId", 123.45);

        when(userRepository.findById(USER_ID))
                .thenReturn(Mono.just(user));
        when(orderWebClientService.findOrdersByPhoneNumberFlux(PHONE_NUMBER, REQUEST_ID))
                .thenReturn(Flux.just(orderDto));
        when(productWebClientService.findProductsByCodeFlux(PRODUCT_CODE, REQUEST_ID))
                .thenReturn(Flux.just(productDto));

        Flux<UserInfoDTO> flux = underTest.getUserInfoById(USER_ID, REQUEST_ID);

        StepVerifier.create(flux)
                .assertNext(actual -> {
                    Assertions.assertEquals(user.getName(), actual.getUserName());
                    Assertions.assertEquals(user.getPhone(), actual.getPhoneNumber());
                    Assertions.assertEquals(orderDto.getOrderNumber(), actual.getOrderNumber());
                    Assertions.assertEquals(productDto.getProductCode(), actual.getProductCode());
                    Assertions.assertEquals(productDto.getProductName(), actual.getProductName());
                    Assertions.assertEquals(productDto.getProductId(), actual.getProductId());
                })
                .verifyComplete();
    }

    @Test
    void getUserInfoById_relevant() {
        var user = generateUser();
        var orderDto = generateOrder();
        var productDto = generateProduct("productId", 123.45);
        var productDto2 = generateProduct("productId2", 234.56);
        var productDto3 = generateProduct("productId3", 0);

        when(userRepository.findById(USER_ID))
                .thenReturn(Mono.just(user));
        when(orderWebClientService.findOrdersByPhoneNumberFlux(PHONE_NUMBER, REQUEST_ID))
                .thenReturn(Flux.just(orderDto));
        when(productWebClientService.findProductsByCodeFlux(PRODUCT_CODE, REQUEST_ID))
                .thenReturn(Flux.just(productDto, productDto2, productDto3));

        var flux = underTest.getUserInfoById(USER_ID, REQUEST_ID);

        StepVerifier.create(flux)
                .assertNext(actual -> Assertions.assertEquals(productDto2.getProductId(), actual.getProductId()))
                .verifyComplete();
    }

    @Test
    void getUserInfoByIdTest_onProductsException() {
        var user = generateUser();
        var orderDto = generateOrder();

        when(userRepository.findById(USER_ID))
                .thenReturn(Mono.just(user));
        when(orderWebClientService.findOrdersByPhoneNumberFlux(PHONE_NUMBER, REQUEST_ID))
                .thenReturn(Flux.just(orderDto));
        when(productWebClientService.findProductsByCodeFlux(PRODUCT_CODE, REQUEST_ID))
                .thenReturn(Flux.error(new Exception()));

        var flux = underTest.getUserInfoById(USER_ID, REQUEST_ID);

        StepVerifier.create(flux)
                .assertNext(actual -> {
                    Assertions.assertEquals(user.getName(), actual.getUserName());
                    Assertions.assertEquals(user.getPhone(), actual.getPhoneNumber());
                    Assertions.assertEquals(orderDto.getOrderNumber(), actual.getOrderNumber());
                    Assertions.assertEquals(orderDto.getProductCode(), actual.getProductCode());
                    Assertions.assertNull(actual.getProductId());
                    Assertions.assertNull(actual.getProductName());
                })
                .verifyComplete();
    }

    @Test
    void getUserInfoByIdTest_noProduct() {
        var user = generateUser();
        var orderDto = generateOrder();

        when(userRepository.findById(USER_ID))
                .thenReturn(Mono.just(user));
        when(orderWebClientService.findOrdersByPhoneNumberFlux(PHONE_NUMBER, REQUEST_ID))
                .thenReturn(Flux.just(orderDto));
        when(productWebClientService.findProductsByCodeFlux(PRODUCT_CODE, REQUEST_ID))
                .thenReturn(Flux.empty());

        var flux = underTest.getUserInfoById(USER_ID, REQUEST_ID);

        StepVerifier.create(flux)
                .assertNext(actual -> {
                    Assertions.assertEquals(user.getName(), actual.getUserName());
                    Assertions.assertEquals(user.getPhone(), actual.getPhoneNumber());
                    Assertions.assertEquals(orderDto.getOrderNumber(), actual.getOrderNumber());
                    Assertions.assertEquals(orderDto.getProductCode(), actual.getProductCode());
                    Assertions.assertNull(actual.getProductId());
                    Assertions.assertNull(actual.getProductName());
                })
                .verifyComplete();
    }

    @Test
    void getUserInfoByIdTest_noOrder() {
        var user = generateUser();
        when(userRepository.findById(USER_ID))
                .thenReturn(Mono.just(user));
        when(this.orderWebClientService.findOrdersByPhoneNumberFlux(PHONE_NUMBER, REQUEST_ID))
                .thenReturn(Flux.empty());

        var flux = underTest.getUserInfoById(USER_ID, REQUEST_ID);

        StepVerifier.create(flux)
                .expectNextCount(0)
                .verifyComplete();

        verify(productWebClientService, never()).findProductsByCodeFlux(PRODUCT_CODE, REQUEST_ID);
    }

    @Test
    void getUserInfoByIdTest_noUser() {
        when(userRepository.findById(USER_ID))
                .thenReturn(Mono.empty());

        var flux = underTest.getUserInfoById(USER_ID, REQUEST_ID);

        StepVerifier.create(flux)
                .expectNextCount(0)
                .verifyComplete();

        verify(orderWebClientService, never()).findOrdersByPhoneNumberFlux(PHONE_NUMBER, REQUEST_ID);
        verify(productWebClientService, never()).findProductsByCodeFlux(PRODUCT_CODE, REQUEST_ID);
    }

    private static User generateUser() {
        User user = new User();
        user.setId(USER_ID);
        user.setName(USER_NAME);
        user.setPhone(PHONE_NUMBER);

        return user;
    }

    private static OrderDTO generateOrder() {
        OrderDTO orderDto = new OrderDTO();
        orderDto.setPhoneNumber(PHONE_NUMBER);
        orderDto.setOrderNumber(ORDER_NUMBER);
        orderDto.setProductCode(PRODUCT_CODE);

        return orderDto;
    }

    private static ProductDTO generateProduct(String productId, double score) {
        ProductDTO productDto = new ProductDTO();
        productDto.setProductId(productId);
        productDto.setProductCode(PRODUCT_CODE);
        productDto.setProductName(PRODUCT_NAME);
        productDto.setScore(score);

        return productDto;
    }
}
