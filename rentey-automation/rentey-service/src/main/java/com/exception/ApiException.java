package com.exception;

/**
 * Custom exception for API operation failures.
 * Contains only the operation name and error message without wrapping the original exception.
 */
public class ApiException extends RuntimeException {

    private final String operation;
    private final String errorMessage;

    public ApiException(String operation, String errorMessage) {
        super("Failed to " + operation + ": " + errorMessage);
        this.operation = operation;
        this.errorMessage = errorMessage;
    }

    public String getOperation() {
        return operation;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

