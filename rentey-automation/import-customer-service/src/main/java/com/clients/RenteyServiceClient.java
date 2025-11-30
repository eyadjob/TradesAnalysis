package com.clients;


import com.beans.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Client service to call all APIs in rentey-service.
 * This service provides methods to interact with all endpoints exposed by rentey-service.
 */
@Service
public class RenteyServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(RenteyServiceClient.class);
    
    private final WebClient renteyServiceWebClient;
    private final String basePath;

    public RenteyServiceClient(
            @Qualifier("renteyServiceWebClient") WebClient renteyServiceWebClient,
            @Value("${rentey.service.api.base-path}") String basePath) {
        this.renteyServiceWebClient = renteyServiceWebClient;
        this.basePath = basePath;
    }

    // ==================== Permission APIs ====================

    /**
     * Get all permissions from rentey-service.
     */
    public Mono<GetAllPermissionsResponseBean> getAllPermissions() {
        return renteyServiceWebClient.get()
                .uri(basePath + "/Permission/GetAllPermissions")
                .retrieve()
                .bodyToMono(GetAllPermissionsResponseBean.class);
    }

    /**
     * Create or update a role in rentey-service.
     */
    public Mono<AbpResponseBean> createOrUpdateRole(Object request) {
        return renteyServiceWebClient.post()
                .uri(basePath + "/Role/CreateOrUpdateRole")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AbpResponseBean.class);
    }

    // ==================== Settings APIs ====================

    /**
     * Update all settings for the tenant.
     */
    public Mono<AbpResponseBean> updateAllSettings(Object request) {
        return renteyServiceWebClient.post()
                .uri(basePath + "/TenantSettings/UpdateAllSettings")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AbpResponseBean.class);
    }

    /**
     * Change tenant settings for a specific country.
     */
    public Mono<AbpResponseBean> changeTenantSettings(Integer countryId, Object request) {
        return renteyServiceWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(basePath + "/GeoSettings/ChangeTenantSettings")
                        .queryParam("countryId", countryId)
                        .build())
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AbpResponseBean.class);
    }

    /**
     * Update country settings for a specific country.
     */
    public Mono<AbpResponseBean> updateCountrySettings(Integer countryId, Object request) {
        return renteyServiceWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(basePath + "/GeoSettings/UpdateCountrySettings")
                        .queryParam("countryId", countryId)
                        .build())
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AbpResponseBean.class);
    }

    /**
     * Change branch settings for a specific country and branch.
     */
    public Mono<AbpResponseBean> changeBranchSettings(Integer countryId, Integer branchId, Object request) {
        return renteyServiceWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(basePath + "/GeoSettings/ChangeBranchSettings")
                        .queryParam("countryId", countryId)
                        .queryParam("branchId", branchId)
                        .build())
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AbpResponseBean.class);
    }

    /**
     * Get operational countries.
     */
    public Mono<GetOperationalCountriesResponseBean> getOperationalCountries() {
        return renteyServiceWebClient.get()
                .uri(basePath + "/Country/GetOperationalCountries")
                .retrieve()
                .bodyToMono(GetOperationalCountriesResponseBean.class);
    }

    // ==================== Country APIs ====================

    /**
     * Get country currency information by country ID.
     */
    public Mono<GetCountryCurrencyInfoResponseBean> getCountryCurrencyInfo(Integer countryId) {
        return renteyServiceWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(basePath + "/Currency/GetCountryCurrencyInfo")
                        .queryParam("countryId", countryId)
                        .build())
                .retrieve()
                .bodyToMono(GetCountryCurrencyInfoResponseBean.class);
    }

    /**
     * Get user branches for combobox.
     */
    public Mono<GetUserBranchesForComboboxResponseBean> getUserBranchesForCombobox(
            Boolean includeInActive,
            Integer countryId,
            Boolean includeAll,
            List<Integer> filterTypes) {
        return renteyServiceWebClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder.path(basePath + "/Branch/GetUserBranchesForCombobox");
                    if (includeInActive != null) {
                        builder.queryParam("includeInActive", includeInActive);
                    }
                    if (countryId != null) {
                        builder.queryParam("countryId", countryId);
                    }
                    if (includeAll != null) {
                        builder.queryParam("includeAll", includeAll);
                    }
                    if (filterTypes != null && !filterTypes.isEmpty()) {
                        for (Integer filterType : filterTypes) {
                            builder.queryParam("filterTypes", filterType);
                        }
                    }
                    return builder.build();
                })
                .retrieve()
                .bodyToMono(GetUserBranchesForComboboxResponseBean.class);
    }

    /**
     * Get countries phone information.
     */
    public Mono<GetCountriesPhoneResponseBean> getCountriesPhone(
            Integer typeId,
            Boolean includeInActive,
            Boolean includeNotAssign) {
        return renteyServiceWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(basePath + "/Country/GetCountriesPhone")
                        .queryParam("typeId", typeId)
                        .queryParam("includeInActive", includeInActive)
                        .queryParam("includeNotAssign", includeNotAssign)
                        .build())
                .retrieve()
                .bodyToMono(GetCountriesPhoneResponseBean.class);
    }

    // ==================== Customer APIs ====================

    /**
     * Create or update a customer.
     */
    public Mono<CreateOrUpdateCustomerResponseBean> createOrUpdateCustomer(CreateOrUpdateCustomerRequestBean request) {
        return renteyServiceWebClient.post()
                .uri(basePath + "/Customer/CreateOrUpdateCustomer")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(CreateOrUpdateCustomerResponseBean.class)
                .doOnError(WebClientResponseException.class, error -> {
                    String responseBody = error.getResponseBodyAsString();
                    logger.error("rentey-service returned error - Status: {}, Response Body: {}", 
                            error.getStatusCode(), responseBody);
                })
                .doOnError(error -> {
                    if (!(error instanceof WebClientResponseException)) {
                        logger.error("Error calling rentey-service: {}", error.getMessage(), error);
                    }
                });
    }

    // ==================== Lookups APIs ====================

    /**
     * Get all items for a combobox based on the lookup type.
     */
    public Mono<GetAllItemsComboboxItemsResponseBean> getAllItemsComboboxItems(
            Integer typeId,
            Boolean includeInActive,
            Boolean includeNotAssign) {
        return renteyServiceWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(basePath + "/Lookups/GetAllItemsComboboxItems")
                        .queryParam("typeId", typeId)
                        .queryParam("includeInActive", includeInActive)
                        .queryParam("includeNotAssign", includeNotAssign)
                        .build())
                .retrieve()
                .bodyToMono(GetAllItemsComboboxItemsResponseBean.class);
    }

    // ==================== File Upload APIs ====================

    /**
     * Upload a base64 encoded file.
     */
    public Mono<UploadBase64FileResponseBean> uploadBase64File(Object request) {
        return renteyServiceWebClient.post()
                .uri(basePath + "/FileUpload/UploadBase64File")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(UploadBase64FileResponseBean.class);
    }
}

