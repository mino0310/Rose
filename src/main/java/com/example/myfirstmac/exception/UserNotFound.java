package com.example.myfirstmac.exception;

public class UserNotFound extends UserException {

    private static final String MESSAGE = "존재하지 않는 회원입니다.";

    @Override
    public int getStatusCode() {
        return 404;
    }

    public UserNotFound() {
        super(MESSAGE);
    }

    public UserNotFound(String fieldName, String message){
        super(MESSAGE);
        super.addValidation(fieldName, message);
    }
}
