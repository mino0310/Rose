package com.example.myfirstmac.exception;

public class Unauthorized extends PostException{
    private static final String MESSAGE = "인증받지 않은 요청입니다.";

    public Unauthorized() {
        super(MESSAGE);
    }
    @Override
    public int getStatusCode() {
        return 401;
    }

}
