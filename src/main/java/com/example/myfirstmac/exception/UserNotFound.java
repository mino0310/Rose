package com.example.myfirstmac.exception;

public class UserNotFound extends RuntimeException {

    private static final String MESSAGE = "존재하지 않는 회원입니다.";

    public UserNotFound() {
        super(MESSAGE);
    }
}
