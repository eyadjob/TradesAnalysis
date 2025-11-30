package com.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;
import java.time.Instant;

/**
 * Standard error response bean for rentey-service.
 * This provides a consistent error response format across all endpoints.
 */
public record ErrorResponseBean(
        @JsonProperty("timestamp") String timestamp,
        @JsonProperty("status") Integer status,
        @JsonProperty("error") String error,
        @JsonProperty("message") String message,
        @JsonProperty("path") String path
) {
    public static ErrorResponseBean create(HttpStatus status, String message, String path) {
        return new ErrorResponseBean(
                Instant.now().toString(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path
        );
    }
    
    public static ErrorResponseBean create(HttpStatus status, String message, String path, Throwable cause) {
        // Extract the most meaningful error message
        String errorMessage = extractErrorMessage(cause, message);
        return new ErrorResponseBean(
                Instant.now().toString(),
                status.value(),
                status.getReasonPhrase(),
                errorMessage,
                path
        );
    }
    
    private static String extractErrorMessage(Throwable throwable, String defaultMessage) {
        if (throwable == null) {
            return defaultMessage != null ? defaultMessage : "An error occurred";
        }
        
        // Check if it's a WebClientResponseException (external API error)
        if (throwable instanceof org.springframework.web.reactive.function.client.WebClientResponseException) {
            org.springframework.web.reactive.function.client.WebClientResponseException webClientEx = 
                    (org.springframework.web.reactive.function.client.WebClientResponseException) throwable;
            String responseBody = webClientEx.getResponseBodyAsString();
            if (responseBody != null && !responseBody.trim().isEmpty()) {
                return String.format("External API error (%s): %s", 
                        webClientEx.getStatusCode(), responseBody);
            }
            return String.format("External API error: %s %s", 
                    webClientEx.getStatusCode(), webClientEx.getMessage());
        }
        
        // Check the cause chain for WebClientResponseException
        Throwable cause = throwable.getCause();
        if (cause instanceof org.springframework.web.reactive.function.client.WebClientResponseException) {
            org.springframework.web.reactive.function.client.WebClientResponseException webClientEx = 
                    (org.springframework.web.reactive.function.client.WebClientResponseException) cause;
            String responseBody = webClientEx.getResponseBodyAsString();
            if (responseBody != null && !responseBody.trim().isEmpty()) {
                return String.format("External API error (%s): %s", 
                        webClientEx.getStatusCode(), responseBody);
            }
            return String.format("External API error: %s %s", 
                    webClientEx.getStatusCode(), webClientEx.getMessage());
        }
        
        // Use the exception message if available, otherwise use default
        String exceptionMessage = throwable.getMessage();
        if (exceptionMessage != null && !exceptionMessage.trim().isEmpty()) {
            return exceptionMessage;
        }
        
        return defaultMessage != null ? defaultMessage : throwable.getClass().getSimpleName();
    }
}

