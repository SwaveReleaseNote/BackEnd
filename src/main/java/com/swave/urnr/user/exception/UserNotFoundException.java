package com.swave.urnr.user.exception;

public class UserNotFoundException  extends Exception {
    private static final String MESSAGE = "올바르지 않은 유저 폼입니다.";

    public UserNotFoundException() {
        super(MESSAGE);
    }

}
