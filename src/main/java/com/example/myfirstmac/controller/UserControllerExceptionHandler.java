package com.example.myfirstmac.controller;


import com.example.myfirstmac.exception.UserException;
import com.example.myfirstmac.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserControllerExceptionHandler {

    @ResponseBody
    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponse> userNotFound(UserException userException) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(String.valueOf(userException.getStatusCode()))
                .message(userException.getMessage())
                .validation(userException.getValidation())
                .build();

        ResponseEntity<ErrorResponse> result = ResponseEntity.status(userException.getStatusCode())
                .body(errorResponse);

        return result;
    }
}
