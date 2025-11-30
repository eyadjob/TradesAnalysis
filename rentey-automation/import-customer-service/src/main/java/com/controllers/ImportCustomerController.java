package com.controllers;

import com.beans.CreateOrUpdateCustomerRequestBean;
import com.beans.CreateOrUpdateCustomerResponseBean;
import com.services.ImportCustomersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            return Mono.just(ResponseEntity
                    .badRequest()
                    .build());
        }

        if (request.customer() == null) {
            return Mono.just(ResponseEntity
                    .badRequest()
                    .build());
        }

        // Call the service to create or update customer
        return importCustomersService.createOrUpdateCustomer(request)
                .map(response -> ResponseEntity
                        .status(HttpStatus.OK)
                        .body(response))
                .onErrorResume(error -> Mono.just(ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build()));
    }
}

