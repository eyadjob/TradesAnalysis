package com.services;

import com.annotation.LogExecutionTime;
import com.annotation.LogRequestAndResponseOnDesk;
import com.beans.CreateOrUpdateCustomerRequestBean;
import com.beans.CreateOrUpdateCustomerResponseBean;
import com.beans.GetAllItemsComboboxItemsResponseBean;
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

    public CustomerService(@Qualifier("settingsWebClient") WebClient webClient, ObjectMapperUtil objectMapperUtil, @Qualifier("apiBasePath") String apiBasePath) {
        this.webClient = webClient;
        this.objectMapperUtil = objectMapperUtil;
        this.apiBasePath = apiBasePath;
    }

    /**
     * Create or update a customer.
     * This method automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     * On error (e.g., 500), returns a response with error message and status code instead of throwing.
     *
     * @param request The request containing customer information.
     * @return The response containing the created or updated customer data, or error response on failure.
     */
    @LogRequestAndResponseOnDesk
    @LogExecutionTime
    public CreateOrUpdateCustomerResponseBean createOrUpdateCustomer(CreateOrUpdateCustomerRequestBean request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null.");
        }

        // Serialize request body for error logging
        final String requestJson = objectMapperUtil.toJsonStringWithLogging(request);


        // Authorization header and all other headers from RenteyConfiguration are automatically included
        return webClient.post().uri(apiBasePath + "/Customer/CreateOrUpdateCustomer").bodyValue(request).retrieve().bodyToMono(CreateOrUpdateCustomerResponseBean.class).doOnError(error -> {
            logger.error("=== Error calling external API ===");
            logger.error("Error message: {}", error.getMessage());
            logger.error("Request Body: {}", requestJson);
            logger.error("Note: Request headers are logged by WebClientLoggingFilter above");
            logger.error("===================================");
        }).block();
    }


    /**
     * Get all items for a combobox based on the lookup type.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param typeId The type ID for the lookup items.
     * @param selectedItemId The selected item ID (optional, can be null).
     * @param includeInActive Whether to include inactive items.
     * @param includeNotAssign Whether to include not assigned items.
     * @return The response containing all combobox items.
     */
    public GetAllItemsComboboxItemsResponseBean getAllItemsComboboxItems(
            Integer typeId,
            Integer selectedItemId,
            Boolean includeInActive,
            Boolean includeNotAssign) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return webClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder
                            .path(apiBasePath + "/Lookups/GetAllItemsComboboxItems")
                            .queryParam("typeId", typeId)
                            .queryParam("includeInActive", includeInActive)
                            .queryParam("includeNotAssign", includeNotAssign);
                    if (selectedItemId != null) {
                        builder.queryParam("selectedItemId", selectedItemId);
                    }
                    return builder.build();
                })
                .retrieve()
                .bodyToMono(GetAllItemsComboboxItemsResponseBean.class)
                .block();
    }
}

