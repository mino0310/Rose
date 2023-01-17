package com.example.myfirstmac.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class PostException extends RuntimeException {

    // 얜 어디로 가는거지. 나중에 얘가 예외 터지면 어떻게 작용해서 어디로 가는지 파악해보자.
    private final Map<String, String> validation = new HashMap<>();
    public PostException(String message) {
        super(message);
    }

    public PostException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();

    protected void addValidation(String fieldName, String message) {
        this.validation.put(fieldName, message);
    }
}
