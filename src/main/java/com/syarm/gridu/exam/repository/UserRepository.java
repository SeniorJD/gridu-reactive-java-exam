package com.syarm.gridu.exam.repository;

import com.syarm.gridu.exam.model.User;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRepository {
    public static final int USERS_COUNT = 5;

    private final Map<Long, User> userMap = new HashMap<>();

    public User getUserById(long id) {
        return userMap.get(id);
    }

    public Mono<User> getUserByIdMono(long id) {
        return Mono.justOrEmpty(userMap.get(id));
    }

    @PostConstruct
    public void fillMap() {
        for (long i = 0; i < USERS_COUNT; i++) {
            userMap.put(i, generateUser(i));
        }
    }

    private User generateUser(long id) {
        return new User(
                id,
                "userName#" + id
        );
    }
}
