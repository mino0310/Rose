package com.example.myfirstmac.exception;


import lombok.Getter;

@Getter
public class InvalidUserRequest extends UserException {

    private static final String MESSAGE = "유효하지 않은 요청입니다.";

    public InvalidUserRequest() {
        super(MESSAGE);
    }

    public InvalidUserRequest(String fieldName, String message){
        super(MESSAGE);
        addValidation(fieldName, message);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}

