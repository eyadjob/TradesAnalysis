package com.services;

import com.annotation.LogRequestAndResponseOnDesk;
import com.beans.CreateOrUpdateCustomerRequestBean;
import com.beans.CreateOrUpdateCustomerResponseBean;
import com.util.ObjectMapperUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final ObjectMapperUtil objectMapperUtil;

    public CustomerService(@Qualifier("settingsWebClient") WebClient webClient, 
                          ObjectMapper objectMapper,
                          ObjectMapperUtil objectMapperUtil) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
        this.objectMapperUtil = objectMapperUtil;
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
    @LogRequestAndResponseOnDesk
    public CreateOrUpdateCustomerResponseBean createOrUpdateCustomer(CreateOrUpdateCustomerRequestBean request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null.");
        }

        // Serialize request body for error logging
        final String requestJson = objectMapperUtil.toJsonStringWithLogging(request);

        try {
            // Authorization header and all other headers from RenteyConfiguration are automatically included
            return webClient.post()
                    .uri("/webapigw/api/services/app/Customer/CreateOrUpdateCustomer")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(CreateOrUpdateCustomerResponseBean.class)
                    .doOnError(error -> {
                        logger.error("=== Error calling external API ===");
                        logger.error("Error message: {}", error.getMessage());
                        logger.error("Request Body: {}", requestJson);
                        logger.error("Note: Request headers are logged by WebClientLoggingFilter above");
                        logger.error("===================================");
                    })
                    .block();

        } catch (WebClientResponseException e) {
            // Extract detailed error information from the response
            String responseBody = e.getResponseBodyAsString();
            logger.error("External API returned error - Status: {}, Response Body: {}", 
                    e.getStatusCode(), responseBody);
            
            // Try to parse the error response to get more details
            String errorMessage = "External API error (" + e.getStatusCode() + ")";
            if (responseBody != null && !responseBody.trim().isEmpty()) {
                try {
                    // Try to extract error message from the response
                    var errorResponse = objectMapper.readTree(responseBody);
                    if (errorResponse.has("error") && errorResponse.get("error").has("message")) {
                        String apiErrorMessage = errorResponse.get("error").get("message").asText();
                        errorMessage += ": " + apiErrorMessage;
                    } else {
                        errorMessage += ": " + responseBody;
                    }
                } catch (Exception parseException) {
                    logger.warn("Could not parse error response: {}", parseException.getMessage());
                    errorMessage += ": " + responseBody;
                }
            } else {
                errorMessage += ": " + e.getMessage();
            }
            
            throw new RuntimeException("Failed to create/update customer: " + errorMessage, e);
            
        } catch (Exception e) {
            logger.error("Unexpected error while creating/updating customer: {}", e.getMessage(), e);
            String errorMessage = "Failed to create/update customer: " + 
                    (e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName());
            throw new RuntimeException(errorMessage, e);
        }
    }
}

