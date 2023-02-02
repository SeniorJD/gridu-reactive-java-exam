package com.syarm.gridu.exam.exceptions;

public class UserNotFoundException extends ApplicationException {
    public UserNotFoundException(Long userId) {
        super("User with id " + userId + " not found");
    }
}
