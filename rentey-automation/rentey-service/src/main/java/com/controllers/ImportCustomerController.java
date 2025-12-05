package com.controllers;

import com.beans.CreateOrUpdateCustomerRequestBean;
import com.beans.CreateOrUpdateCustomerResponseBean;
import com.services.ImportCustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import static com.controllers.ApiPaths.BASE_PATH;

/**
 * Controller for importing customer data.
 * This controller provides REST endpoints to create or update customer records
 * by calling the CustomerService directly (since it's in the same service).
 */
@RestController
@RequestMapping(path = BASE_PATH)
public class ImportCustomerController {

    private static final Logger logger = LoggerFactory.getLogger(ImportCustomerController.class);
    @Autowired
    ImportCustomerService importCustomerService;


    /**
     * import Customer
     * This endpoint accepts a CreateOrUpdateCustomerRequestBean payload and calls
     * the CustomerService directly to create or update the customer record.
     *
     * @param request The request containing customer information.
     * @return ResponseEntity containing the response from the createOrUpdateCustomer API
     */
    @PostMapping(
            path = "/import-customer", 
            consumes = "application/json", 
            produces = "application/json"
    )
    public ResponseEntity<Object> importCustomer(
            @RequestBody(required = true) CreateOrUpdateCustomerRequestBean request) {

        // Validate request
        if (request == null) {
            logger.warn("Request body is null");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Request body is required and cannot be null.");
        }

        if (request.customer() == null) {
            logger.warn("Customer data is null");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Customer data is required and cannot be null.");
        }

        try {
            // Call the service directly to create or update customer
            CreateOrUpdateCustomerResponseBean response = importCustomerService.importCustomer(request);
            logger.info("Successfully created/updated customer");
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(response);
        } catch (Exception error) {
            String errorMessage = error.getMessage();
            logger.error("Error creating/updating customer: {}", errorMessage, error);
            
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorMessage != null ? errorMessage : "An error occurred while creating/updating customer");
        }
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

