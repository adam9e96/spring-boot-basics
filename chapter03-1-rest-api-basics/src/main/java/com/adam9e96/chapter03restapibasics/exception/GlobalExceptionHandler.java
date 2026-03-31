package com.adam9e96.chapter03restapibasics.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

// 예외를 HTTP 응답으로 바꾸는 가장 기본적인 형태의 전역 예외 처리기다.
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TodoNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleTodoNotFound(TodoNotFoundException exception) {
        return Map.of("message", exception.getMessage());
    }
}
