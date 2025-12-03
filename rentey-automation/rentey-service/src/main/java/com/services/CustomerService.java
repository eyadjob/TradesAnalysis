package com.services;

import com.annotation.HandleApiException;
import com.annotation.LogRequestAndResponseOnDesk;
import com.beans.CreateOrUpdateCustomerRequestBean;
import com.beans.CreateOrUpdateCustomerResponseBean;
import com.util.ObjectMapperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    private final WebClient webClient;
    private final ObjectMapperUtil objectMapperUtil;
    private final String apiBasePath;

    public CustomerService(@Qualifier("settingsWebClient") WebClient webClient,
                          ObjectMapperUtil objectMapperUtil,
                          @Qualifier("apiBasePath") String apiBasePath) {
        this.webClient = webClient;
        this.objectMapperUtil = objectMapperUtil;
        this.apiBasePath = apiBasePath;
    }

    /**
     * Create or update a customer.
     * This method automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     * Exception handling is performed automatically by the ApiExceptionHandlingAspect.
     *
     * @param request The request containing customer information.
     * @return The response containing the created or updated customer data.
     * @throws RuntimeException if the external API call fails
     */
    @LogRequestAndResponseOnDesk
//    @HandleApiException(operation = "create/update customer")
    public CreateOrUpdateCustomerResponseBean createOrUpdateCustomer(CreateOrUpdateCustomerRequestBean request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null.");
        }

        // Serialize request body for error logging
        final String requestJson = objectMapperUtil.toJsonStringWithLogging(request);

        // Authorization header and all other headers from RenteyConfiguration are automatically included
        return webClient.post()
                .uri(apiBasePath + "/Customer/CreateOrUpdateCustomer")
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
    }
}

