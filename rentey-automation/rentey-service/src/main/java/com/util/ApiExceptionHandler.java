package com.util;

import com.exception.ApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * Utility class for handling and logging exceptions from API calls.
 * Provides common exception handling logic that can be reused across services.
 */
@Component
public class ApiExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);
    private final ObjectMapper objectMapper;

    public ApiExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Handles WebClientResponseException by extracting detailed error information
     * and throwing an ApiException with only the operation and error message.
     *
     * @param e           The WebClientResponseException to handle
     * @param operation   A description of the operation that failed (e.g., "create/update customer")
     * @throws ApiException with operation and error message only
     */
    public void handleWebClientResponseException(WebClientResponseException e, String operation) {
        String responseBody = e.getResponseBodyAsString();
        logger.error("External API returned error - Status: {}, Response Body: {}",
                e.getStatusCode(), responseBody);

        String errorMessage = buildErrorMessage(e, responseBody);
        throw new ApiException(operation, errorMessage);
    }

    /**
     * Handles generic exceptions from API calls by logging and throwing a RuntimeException.
     *
     * @param e         The exception to handle
     * @param operation A description of the operation that failed (e.g., "create/update customer")
     * @throws RuntimeException with error message
     */
    public void handleGenericException(Exception e, String operation) {
        logger.error("Unexpected error while {}: {}", operation, e.getMessage(), e);
        String errorMessage = "Failed to " + operation + ": " +
                (e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName());
        throw new RuntimeException(errorMessage, e);
    }

    /**
     * Builds an error message from a WebClientResponseException.
     * Attempts to parse the response body to extract a meaningful error message.
     *
     * @param e            The WebClientResponseException
     * @param responseBody The response body as string
     * @return A formatted error message
     */
    private String buildErrorMessage(WebClientResponseException e, String responseBody) {
        String errorMessage = "External API error (" + e.getStatusCode() + ")";

        if (responseBody != null && !responseBody.trim().isEmpty()) {
            try {
                var errorResponse = objectMapper.readTree(responseBody);
                if (errorResponse.has("error") && errorResponse.get("error").has("message")) {
                    String apiErrorMessage = errorResponse.get("error").get("message").asText();
                    errorMessage += ": " + apiErrorMessage;
                } else {
                    errorMessage += ": " + responseBody;
                }
            } catch (Exception parseException) {
                logger.warn("Could not parse error response: {}", parseException.getMessage());
                errorMessage += ": " + responseBody;
            }
        } else {
            errorMessage += ": " + e.getMessage();
        }

        return errorMessage;
    }
}

