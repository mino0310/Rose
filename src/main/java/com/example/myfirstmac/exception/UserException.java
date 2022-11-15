package com.example.myfirstmac.exception;

public abstract class UserException extends RuntimeException{

    public UserException(String message) {
        super(message);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();
}
