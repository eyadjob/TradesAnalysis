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
    public Mono<ResponseEntity<CreateOrUpdateCustomerResponseBean>> createOrUpdateCustomer(
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
                .map(response -> {
                    logger.info("Successfully created/updated customer");
                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .body(response);
                })
                .onErrorResume(error -> {
                    logger.error("Error creating/updating customer: {}", error.getMessage(), error);
                    return Mono.just(ResponseEntity
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .build());
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

