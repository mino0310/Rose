package com.example.myfirstmac.controller;


import com.example.myfirstmac.exception.InvalidUserRequest;
import com.example.myfirstmac.exception.UserException;
import com.example.myfirstmac.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class UserControllerExceptionHandler {

    // MethodArgumentNotValidException 처럼 스프링에서 제공해주는 예외는 따로 등록해두는 것이 좋다
    // 비즈니스 관련된 것도 최상위 비즈니스 로직과 관련된 예외를 등록해놓는 것이 좋다.

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handle(MethodArgumentNotValidException e) {
//        ErrorResponse errorResponse = new ErrorResponse("400", "잘못된 요청입니다.");
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("400")
                .message("잘못된 요청입니다")
                .validation(new HashMap<String, String>())
                .build();

        List<FieldError> fieldErrors = e.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            errorResponse.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return errorResponse;
    }

    @ResponseBody
    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponse> userNotFound(UserException e) {

        int statusCode = e.getStatusCode();
        Map<String, String> validation = e.getValidation();

        ErrorResponse body = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .validation(e.getValidation())
                .message(e.getMessage())
                .build();

/*        // 많은 예외를 다음처럼 하나씩 처리하는 건 비효율적이다.
        if (e instanceof UserException) {
            // 에외를 꺼내야 한다.
            InvalidUserRequest invalidRequest = (InvalidUserRequest) e;
            String fieldName = invalidRequest.getFieldName();
            String message = invalidRequest.getMessage();

            body.addValidation(fieldName, message);
        }*/

        ResponseEntity<ErrorResponse> response = ResponseEntity.status(statusCode)
                .body(body);

        return response;
    }

}

