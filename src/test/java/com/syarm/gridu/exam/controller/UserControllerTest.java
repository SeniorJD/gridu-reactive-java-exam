package com.syarm.gridu.exam.controller;

import com.syarm.gridu.exam.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ComponentScan(basePackages = "com.syarm.gridu.exam")
@WebMvcTest(value = UserController.class)
public class UserControllerTest {
    @Autowired
    protected MockMvc mvc;

    @MockBean
    private UserService userService;

    @Test
    void getOrderInfoTest() throws Exception {
        var id = 1L;
        mvc.perform(get("/v1/user/" + id))
                .andExpect(status().isOk());

        verify(userService).getUserById(id);
    }
}
