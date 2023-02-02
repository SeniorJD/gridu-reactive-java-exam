package com.syarm.gridu.exam.repository;

import com.syarm.gridu.exam.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {
    private UserRepository underTest;

    @BeforeEach
    public void setUp() {
        underTest = new UserRepository();
        underTest.fillMap();
    }

    @Test
    public void getUserById() {
        for (int i = 0; i < UserRepository.USERS_COUNT; i++) {
            User userById = underTest.getUserById(i);
            assertNotNull(userById);
        }

    }

    @Test
    public void getUserById_null() {
        User userById = underTest.getUserById(UserRepository.USERS_COUNT);
        assertNull(userById);
    }

    @Test
    public void getUserByIdMono() {
        for (int i = 0; i < UserRepository.USERS_COUNT; i++) {
            StepVerifier.create(underTest.getUserByIdMono(i))
                    .expectNextCount(1)
                    .verifyComplete();
        }
    }

    @Test
    public void getUserByIdMono_null() {
        StepVerifier.create(underTest.getUserByIdMono(UserRepository.USERS_COUNT))
                .expectNextCount(0)
                .verifyComplete();
    }
}
