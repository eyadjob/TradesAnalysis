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

    public ImportCustomersService(RenteyServiceClient renteyServiceClient) {
        this.renteyServiceClient = renteyServiceClient;
    }

    /**
     * Creates or updates a customer with default dummy data.
     * This method uses CustomerDataBuilder with default values from the JSON payload
     * and calls the rentey-service API.
     *
     * @return Mono containing the response from the createOrUpdateCustomer API
     */
    public Mono<CreateOrUpdateCustomerResponseBean> createOrUpdateCustomer() {
        // Build the request with default dummy data using CustomerDataBuilder
        CreateOrUpdateCustomerRequestBean request = CustomerDataBuilder.create()
                .build();

        // Call the rentey-service API
        return renteyServiceClient.createOrUpdateCustomer(request);
    }

    /**
     * Creates or updates a customer with custom data.
     * This method uses CustomerDataBuilder and allows customization of any field.
     *
     * @param firstName Custom first name (null to use default)
     * @param email Custom email (null to use default)
     * @return Mono containing the response from the createOrUpdateCustomer API
     */
    public Mono<CreateOrUpdateCustomerResponseBean> createOrUpdateCustomerWithCustomData(String firstName, String email) {
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
     *
     * @param builder Customized CustomerDataBuilder
     * @return Mono containing the response from the createOrUpdateCustomer API
     */
    public Mono<CreateOrUpdateCustomerResponseBean> createOrUpdateCustomer(CustomerDataBuilder builder) {
        CreateOrUpdateCustomerRequestBean request = builder.build();
        return renteyServiceClient.createOrUpdateCustomer(request);
    }

    /**
     * Creates or updates a customer with the provided request.
     * This method accepts a CreateOrUpdateCustomerRequestBean and calls the rentey-service API.
     *
     * @param request The request containing customer information.
     * @return Mono containing the response from the createOrUpdateCustomer API
     */
    public Mono<CreateOrUpdateCustomerResponseBean> createOrUpdateCustomer(CreateOrUpdateCustomerRequestBean request) {
        return renteyServiceClient.createOrUpdateCustomer(request);
    }
}
