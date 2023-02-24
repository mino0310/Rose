package com.example.myfirstmac.exception;

public class AlreadyEmailExist extends UserException {

    private static final String MESSAGE = "이미 존재하는 이메일입니다.";

    @Override
    public int getStatusCode() {
        return 400;
    }

    public AlreadyEmailExist() {
        super(MESSAGE);
    }

    public AlreadyEmailExist(String fieldName, String message){
        super(MESSAGE);
        super.addValidation(fieldName, message);
    }
}
