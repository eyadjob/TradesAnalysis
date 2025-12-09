package com;

import com.beans.general.ErrorResponseBean;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.NoSuchElementException;

/**
 * Global exception handler for rentey-service.
 * Provides consistent error responses across all endpoints.
 */
@RestControllerAdvice(basePackages = "com.controllers")
public class RenteyControllerExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(RenteyControllerExceptionHandler.class);

    static {
        logger.info("RenteyControllerExceptionHandler initialized and registered");
    }

    // Constructor to ensure the bean is created
    public RenteyControllerExceptionHandler() {
        logger.info("RenteyControllerExceptionHandler bean created");
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponseBean> notFound(NoSuchElementException ex, HttpServletRequest request) {
        logger.warn("Resource not found: {}", ex.getMessage());
        ErrorResponseBean errorResponse = ErrorResponseBean.create(
                HttpStatus.NOT_FOUND,
                ex.getMessage() != null ? ex.getMessage() : "Resource not found",
                request.getRequestURI(),
                ex
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseBean> badRequest(HttpMessageNotReadableException ex, HttpServletRequest request) {
        String exMessage = ex.getMessage();
        String message = "Invalid request body. Please ensure the JSON is complete and properly formatted. " +
                         (exMessage != null ? exMessage : "Request body may be empty or malformed.");
        logger.warn("Invalid request body: {}", message);
        ErrorResponseBean errorResponse = ErrorResponseBean.create(
                HttpStatus.BAD_REQUEST,
                message,
                request.getRequestURI(),
                ex
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseBean> badRequest(IllegalArgumentException ex, HttpServletRequest request) {
        logger.warn("Invalid argument: {}", ex.getMessage());
        ErrorResponseBean errorResponse = ErrorResponseBean.create(
                HttpStatus.BAD_REQUEST,
                ex.getMessage() != null ? ex.getMessage() : "Invalid argument",
                request.getRequestURI(),
                ex
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseBean> handleRuntimeException(RuntimeException ex, HttpServletRequest request) {
        String exMessage = ex.getMessage();
        logger.error("RuntimeException in controller - Message: {}, URI: {}", exMessage, request.getRequestURI(), ex);

        // Check if it's a WebClientResponseException wrapped in RuntimeException
        Throwable cause = ex.getCause();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = exMessage != null ? exMessage : "Internal server error";

        if (cause instanceof WebClientResponseException) {
            WebClientResponseException webClientEx = (WebClientResponseException) cause;
            status = HttpStatus.valueOf(webClientEx.getStatusCode().value());
            // Extract detailed error message from the external API response
            String responseBody = webClientEx.getResponseBodyAsString();
            if (responseBody != null && !responseBody.trim().isEmpty()) {
                message = String.format("External API error (%s): %s",
                        webClientEx.getStatusCode(), responseBody);
            } else {
                message = exMessage != null ? exMessage :
                        String.format("External API error: %s %s",
                                webClientEx.getStatusCode(), webClientEx.getMessage());
            }
            logger.error("Extracted WebClientResponseException - Status: {}, Message: {}", status, message);
        } else if (exMessage != null && exMessage.contains("External API error")) {
            // If the message already contains external API error info, use it as-is
            message = exMessage;
            logger.error("Using existing External API error message: {}", message);
        }

        ErrorResponseBean errorResponse = ErrorResponseBean.create(
                status,
                message,
                request.getRequestURI(),
                ex
        );

        logger.error("Returning ErrorResponseBean - Status: {}, Message: {}", status, message);
        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseBean> handleGenericException(Exception ex, HttpServletRequest request) {
        logger.error("Unexpected exception in controller: {}", ex.getMessage(), ex);
        ErrorResponseBean errorResponse = ErrorResponseBean.create(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred",
                request.getRequestURI(),
                ex
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}

