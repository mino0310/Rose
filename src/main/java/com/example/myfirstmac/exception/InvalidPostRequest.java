package com.example.myfirstmac.exception;


import lombok.Getter;

@Getter
public class InvalidPostRequest extends PostException {

    private static final String MESSAGE = "유효하지 않은 요청입니다.";

    public InvalidPostRequest() {
        super(MESSAGE);
    }

    public InvalidPostRequest(String fieldName, String message){
        super(MESSAGE);
        super.addValidation(fieldName, message);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}

