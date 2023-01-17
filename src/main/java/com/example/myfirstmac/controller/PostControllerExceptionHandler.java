package com.example.myfirstmac.controller;

import com.example.myfirstmac.exception.PostException;
import com.example.myfirstmac.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PostControllerExceptionHandler {

    @ResponseBody
    @ExceptionHandler(PostException.class)
    public ResponseEntity<ErrorResponse> postNotFound(PostException e) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(String.valueOf(e.getStatusCode()))
                .message(e.getMessage())
                .build();

        ResponseEntity<ErrorResponse> response = ResponseEntity.status(e.getStatusCode())
                .body(errorResponse);

        return response;
    }
}
