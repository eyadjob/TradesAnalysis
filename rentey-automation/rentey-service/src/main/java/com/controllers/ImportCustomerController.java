package com.controllers;

import com.beans.customer.CreateOrUpdateCustomerResponseBean;
import com.services.ImportCustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.controllers.ApiPaths.*;

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
     * @return ResponseEntity containing the response from the createOrUpdateCustomer API
     */
    @GetMapping(
            path = IMPORT_CUSTOMER_FROM_CSV_FILE
    )
    public ResponseEntity<Object> importCustomer() {

        try {
            // Call the service directly to create or update customer
            CreateOrUpdateCustomerResponseBean response = importCustomerService.importCustomerRecordsToSystemFromCsvFile();
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

}

