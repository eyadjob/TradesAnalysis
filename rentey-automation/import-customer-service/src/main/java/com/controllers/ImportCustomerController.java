package com.controllers;

import com.beans.CreateOrUpdateCustomerRequestBean;
import com.beans.CreateOrUpdateCustomerResponseBean;
import com.services.ImportCustomersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * Controller for importing customer data.
 * This controller provides REST endpoints to create or update customer records
 * by calling the rentey-service API.
 */
@RestController
@RequestMapping(path = "/api/import-customer")
public class ImportCustomerController {

    private static final Logger logger = LoggerFactory.getLogger(ImportCustomerController.class);
    private final ImportCustomersService importCustomersService;

    public ImportCustomerController(ImportCustomersService importCustomersService) {
        this.importCustomersService = importCustomersService;
    }

    /**
     * Create or update a customer.
     * This endpoint accepts a CreateOrUpdateCustomerRequestBean payload and calls
     * the rentey-service API to create or update the customer record.
     *
     * @param request The request containing customer information.
     * @return ResponseEntity containing the response from the createOrUpdateCustomer API
     */
    @PostMapping(path = "/customer", consumes = "application/json", produces = "application/json")
    public Mono<ResponseEntity<Object>> createOrUpdateCustomer(
            @RequestBody(required = true) CreateOrUpdateCustomerRequestBean request) {

        // Validate request
        if (request == null) {
            logger.warn("Request body is null");
            return Mono.just(ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }

        if (request.customer() == null) {
            logger.warn("Customer data is null");
            return Mono.just(ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }

        // Call the service to create or update customer
        return importCustomersService.createOrUpdateCustomer(request)
                .map((CreateOrUpdateCustomerResponseBean response) -> {
                    logger.info("Successfully created/updated customer");
                    ResponseEntity<Object> responseEntity = ResponseEntity
                            .status(HttpStatus.OK)
                            .body((Object) response);
                    return responseEntity;
                })
                .onErrorResume(error -> {
                    String errorMessage = error.getMessage();
                    logger.error("Error creating/updating customer: {}", errorMessage, error);
                    
                    // Check if it's a WebClientResponseException to get more details
                    if (error instanceof org.springframework.web.reactive.function.client.WebClientResponseException) {
                        org.springframework.web.reactive.function.client.WebClientResponseException webClientEx = 
                                (org.springframework.web.reactive.function.client.WebClientResponseException) error;
                        String responseBody = webClientEx.getResponseBodyAsString();
                        logger.error("rentey-service error details - Status: {}, Response: {}", 
                                webClientEx.getStatusCode(), responseBody);
                        errorMessage = String.format("rentey-service error: %s. Response: %s", 
                                webClientEx.getStatusCode(), 
                                responseBody != null ? responseBody : "No response body");
                    }
                    
                    ResponseEntity<Object> errorResponse = ResponseEntity
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body((Object) errorMessage);
                    return Mono.just(errorResponse);
                });
    }

    /**
     * Exception handler for JSON parsing errors.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        logger.error("JSON parsing error: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Invalid JSON format: " + ex.getMessage());
    }
}

