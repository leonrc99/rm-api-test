package com.reliquiasdamagia.api_rm.security;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "http://localhost:4200");
        headers.add("Access-Control-Allow-Credentials", "true");

        return new ResponseEntity<>(ex.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
