package com.controllers;

import com.beans.customer.CreateOrUpdateCustomerRequestBean;
import com.beans.customer.CreateOrUpdateCustomerResponseBean;
import com.services.CustomerOperationsService;
import com.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static com.controllers.ApiPaths.*;

@RestController
@RequestMapping(path = BASE_PATH)
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerOperationsService customerOperationsService;
    /**
     * Create or update a customer.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param request The request containing customer information.
     * @return The response containing the created or updated customer data.
     */
    @PostMapping(path = CUSTOMER_CREATE_OR_UPDATE, consumes = "application/json", produces = "application/json")
    public CreateOrUpdateCustomerResponseBean createOrUpdateCustomer(
            @RequestBody(required = true) CreateOrUpdateCustomerRequestBean request) {

        if (request == null) {
            throw new IllegalArgumentException("Request body is required and cannot be null.");
        }

        if (request.customer() == null) {
            throw new IllegalArgumentException("Customer data is required and cannot be null.");
        }

        return customerService.createOrUpdateCustomer(request);
    }


    /**
     * Create a customer with random data.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param countryName The name of the country for the customer
     * @return The response containing the created customer data.
     */
    @GetMapping(path = CUSTOMER_CREATE_WITH_RANDOM_NAME, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.ALL_VALUE})
    public CreateOrUpdateCustomerResponseBean createOrUpdateCustomerWithRandomName(@RequestParam(required = true) String countryName) {
        if (countryName == null || countryName.trim().isEmpty()) {
            throw new IllegalArgumentException("countryName parameter is required and cannot be empty.");
        }
        return customerOperationsService.createCustomerWithRandomData(countryName);
    }


}

