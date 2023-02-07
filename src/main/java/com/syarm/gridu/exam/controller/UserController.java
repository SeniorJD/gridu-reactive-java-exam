package com.syarm.gridu.exam.controller;

import com.syarm.gridu.exam.model.dto.UserInfoDTO;
import com.syarm.gridu.exam.service.UserService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/v1/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<UserInfoDTO> getOrderInfo(@PathVariable("id") String userId,
                                          @RequestHeader("REQUEST_ID") String requestId
    ) {
        if (StringUtils.isEmpty(requestId)) {
            throw new IllegalStateException();
        }
        return userService.getUserInfoById(userId, requestId);
    }
}
