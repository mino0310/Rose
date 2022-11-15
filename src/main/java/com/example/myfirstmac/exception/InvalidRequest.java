package com.example.myfirstmac.exception;

public class InvalidRequest extends UserException {

    private static final String MESSAGE = "유효하지 않은 요청입니다.";

    public InvalidRequest() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}

