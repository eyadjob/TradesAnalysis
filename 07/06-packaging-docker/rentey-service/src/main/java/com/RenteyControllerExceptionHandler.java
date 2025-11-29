package com;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class RenteyControllerExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ErrorResponse notFound(NoSuchElementException ex) {
        return ErrorResponse.create(ex, HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorResponse badRequest(HttpMessageNotReadableException ex) {
        String message = "Invalid request body. Please ensure the JSON is complete and properly formatted. " + 
                         (ex.getMessage() != null ? ex.getMessage() : "Request body may be empty or malformed.");
        return ErrorResponse.create(ex, HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponse badRequest(IllegalArgumentException ex) {
        return ErrorResponse.create(ex, HttpStatus.BAD_REQUEST, ex.getMessage());
    }
}

