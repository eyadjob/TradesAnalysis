package com.services;

import com.beans.CreateOrUpdateCustomerRequestBean;
import com.beans.CreateOrUpdateCustomerResponseBean;
import com.builders.CustomerDataBuilder;
import com.clients.RenteyServiceClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ImportCustomersService {

    private final RenteyServiceClient renteyServiceClient;
    private final RenteyServiceManager renteyServiceManager;

    public ImportCustomersService(
            RenteyServiceClient renteyServiceClient,
            RenteyServiceManager renteyServiceManager) {
        this.renteyServiceClient = renteyServiceClient;
        this.renteyServiceManager = renteyServiceManager;
    }

    /**
     * Creates or updates a customer with default dummy data.
     * This method uses CustomerDataBuilder with default values from the JSON payload
     * and calls the rentey-service API.
     * It automatically ensures rentey-service is running before making the call.
     *
     * @return Mono containing the response from the createOrUpdateCustomer API
     */
    public Mono<CreateOrUpdateCustomerResponseBean> createOrUpdateCustomer() {
        // Ensure rentey-service is running before making the call
        renteyServiceManager.ensureRenteyServiceIsRunning();
        
        // Build the request with default dummy data using CustomerDataBuilder
        CreateOrUpdateCustomerRequestBean request = CustomerDataBuilder.create()
                .build();

        // Call the rentey-service API
        return renteyServiceClient.createOrUpdateCustomer(request);
    }

    /**
     * Creates or updates a customer with custom data.
     * This method uses CustomerDataBuilder and allows customization of any field.
     * It automatically ensures rentey-service is running before making the call.
     *
     * @param firstName Custom first name (null to use default)
     * @param email Custom email (null to use default)
     * @return Mono containing the response from the createOrUpdateCustomer API
     */
    public Mono<CreateOrUpdateCustomerResponseBean> createOrUpdateCustomerWithCustomData(String firstName, String email) {
        // Ensure rentey-service is running before making the call
        renteyServiceManager.ensureRenteyServiceIsRunning();
        
        // Build the request with custom data using CustomerDataBuilder
        CustomerDataBuilder builder = CustomerDataBuilder.create();
        
        if (firstName != null) {
            builder.withFirstName(firstName);
        }
        if (email != null) {
            builder.withEmail(email);
        }
        
        CreateOrUpdateCustomerRequestBean request = builder.build();

        // Call the rentey-service API
        return renteyServiceClient.createOrUpdateCustomer(request);
    }

    /**
     * Creates or updates a customer using a custom builder.
     * This method allows full control over the CustomerDataBuilder.
     * It automatically ensures rentey-service is running before making the call.
     *
     * @param builder Customized CustomerDataBuilder
     * @return Mono containing the response from the createOrUpdateCustomer API
     */
    public Mono<CreateOrUpdateCustomerResponseBean> createOrUpdateCustomer(CustomerDataBuilder builder) {
        // Ensure rentey-service is running before making the call
        renteyServiceManager.ensureRenteyServiceIsRunning();
        
        CreateOrUpdateCustomerRequestBean request = builder.build();
        return renteyServiceClient.createOrUpdateCustomer(request);
    }

    /**
     * Creates or updates a customer with the provided request.
     * This method accepts a CreateOrUpdateCustomerRequestBean and calls the rentey-service API.
     * It automatically ensures rentey-service is running before making the call.
     *
     * @param request The request containing customer information.
     * @return Mono containing the response from the createOrUpdateCustomer API
     */
    public Mono<CreateOrUpdateCustomerResponseBean> createOrUpdateCustomer(CreateOrUpdateCustomerRequestBean request) {
        // Ensure rentey-service is running before making the call
        renteyServiceManager.ensureRenteyServiceIsRunning();
        return renteyServiceClient.createOrUpdateCustomer(request);
    }
}
