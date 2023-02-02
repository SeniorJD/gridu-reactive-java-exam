package com.syarm.gridu.exam.controller;

import com.syarm.gridu.exam.model.dto.UserOrder;
import com.syarm.gridu.exam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/v1/user/{id}")
    public Flux<UserOrder> getOrderInfo(@PathVariable("id") Long orderId) {
        return userService.getUserById(orderId);
    }
}
