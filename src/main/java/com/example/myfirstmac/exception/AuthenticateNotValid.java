package com.example.myfirstmac.exception;

public class AuthenticateNotValid extends UserException{

    private static final String MESSAGE = "아이디 혹은 비밀번호가 올바르지 않습니다.";

    @Override
    public int getStatusCode() {
        return 400;
    }

    public AuthenticateNotValid() {
        super(MESSAGE);
    }

    public AuthenticateNotValid(String fieldName, String message){
        super(MESSAGE);
        super.addValidation(fieldName, message);
    }
}
