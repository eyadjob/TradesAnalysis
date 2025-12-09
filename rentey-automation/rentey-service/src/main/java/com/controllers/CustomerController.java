package com.controllers;

import com.beans.customer.CreateOrUpdateCustomerRequestBean;
import com.beans.customer.CreateOrUpdateCustomerResponseBean;
import com.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.controllers.ApiPaths.*;

@RestController
@RequestMapping(path = BASE_PATH)
public class CustomerController {

    @Autowired
    private CustomerService customerService;

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


}

