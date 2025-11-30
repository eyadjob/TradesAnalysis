package com.services;

import com.beans.CreateOrUpdateCustomerRequestBean;
import com.beans.CreateOrUpdateCustomerResponseBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);
    
    private final WebClient settingsWebClient;
    private final AuthorizationTokenService authorizationTokenService;

    public CustomerService(
            @Qualifier("settingsWebClient") WebClient settingsWebClient,
            AuthorizationTokenService authorizationTokenService) {
        this.settingsWebClient = settingsWebClient;
        this.authorizationTokenService = authorizationTokenService;
    }

    /**
     * Create or update a customer.
     * This method automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param request The request containing customer information.
     * @return The response containing the created or updated customer data.
     * @throws RuntimeException if the external API call fails
     */
    public CreateOrUpdateCustomerResponseBean createOrUpdateCustomer(CreateOrUpdateCustomerRequestBean request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null.");
        }
        
        try {
            String refreshToken = authorizationTokenService.getRefreshToken();
            String authorization = "Bearer " + refreshToken;
            
            logger.debug("Calling external API to create/update customer");
            
            return settingsWebClient.post()
                    .uri("/webapigw/api/services/app/Customer/CreateOrUpdateCustomer")
                    .header("Authorization", authorization)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(CreateOrUpdateCustomerResponseBean.class)
                    .doOnError(error -> {
                        logger.error("Error calling external API: {}", error.getMessage(), error);
                    })
                    .block();
        } catch (WebClientResponseException e) {
            String responseBody = e.getResponseBodyAsString();
            logger.error("External API returned error - Status: {}, Response Body: {}", 
                    e.getStatusCode(), responseBody);
            
            // Create a detailed error message that will be included in the response
            String errorMessage = String.format(
                    "External API error (%s): %s",
                    e.getStatusCode(),
                    responseBody != null && !responseBody.trim().isEmpty() 
                            ? responseBody 
                            : "No response body from external API"
            );
            
            logger.error("Throwing RuntimeException with message: {}", errorMessage);
            throw new RuntimeException(errorMessage, e);
        } catch (Exception e) {
            logger.error("Unexpected error while creating/updating customer: {}", e.getMessage(), e);
            String errorMessage = "Failed to create/update customer: " + 
                    (e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName());
            throw new RuntimeException(errorMessage, e);
        }
    }
}

