package com.clients;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * Client service to call all APIs in rentey-service.
 * This service provides methods to interact with all endpoints exposed by rentey-service.
 */
@Service
public class RenteyServiceClient {

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
    public Mono<Object> getAllPermissions() {
        return renteyServiceWebClient.get()
                .uri(basePath + "/Permission/GetAllPermissions")
                .retrieve()
                .bodyToMono(Object.class);
    }

    /**
     * Create or update a role in rentey-service.
     */
    public Mono<Object> createOrUpdateRole(Object request) {
        return renteyServiceWebClient.post()
                .uri(basePath + "/Role/CreateOrUpdateRole")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Object.class);
    }

    // ==================== Settings APIs ====================

    /**
     * Update all settings for the tenant.
     */
    public Mono<Object> updateAllSettings(Object request) {
        return renteyServiceWebClient.post()
                .uri(basePath + "/TenantSettings/UpdateAllSettings")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Object.class);
    }

    /**
     * Change tenant settings for a specific country.
     */
    public Mono<Object> changeTenantSettings(Integer countryId, Object request) {
        return renteyServiceWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(basePath + "/GeoSettings/ChangeTenantSettings")
                        .queryParam("countryId", countryId)
                        .build())
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Object.class);
    }

    /**
     * Update country settings for a specific country.
     */
    public Mono<Object> updateCountrySettings(Integer countryId, Object request) {
        return renteyServiceWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(basePath + "/GeoSettings/UpdateCountrySettings")
                        .queryParam("countryId", countryId)
                        .build())
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Object.class);
    }

    /**
     * Change branch settings for a specific country and branch.
     */
    public Mono<Object> changeBranchSettings(Integer countryId, Integer branchId, Object request) {
        return renteyServiceWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(basePath + "/GeoSettings/ChangeBranchSettings")
                        .queryParam("countryId", countryId)
                        .queryParam("branchId", branchId)
                        .build())
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Object.class);
    }

    /**
     * Get operational countries.
     */
    public Mono<Object> getOperationalCountries() {
        return renteyServiceWebClient.get()
                .uri(basePath + "/Country/GetOperationalCountries")
                .retrieve()
                .bodyToMono(Object.class);
    }

    // ==================== Country APIs ====================

    /**
     * Get country currency information by country ID.
     */
    public Mono<Object> getCountryCurrencyInfo(Integer countryId) {
        return renteyServiceWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(basePath + "/Currency/GetCountryCurrencyInfo")
                        .queryParam("countryId", countryId)
                        .build())
                .retrieve()
                .bodyToMono(Object.class);
    }

    /**
     * Get user branches for combobox.
     */
    public Mono<Object> getUserBranchesForCombobox(
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
                .bodyToMono(Object.class);
    }

    /**
     * Get countries phone information.
     */
    public Mono<Object> getCountriesPhone(
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
                .bodyToMono(Object.class);
    }

    // ==================== Customer APIs ====================

    /**
     * Create or update a customer.
     */
    public Mono<Object> createOrUpdateCustomer(Object request) {
        return renteyServiceWebClient.post()
                .uri(basePath + "/Customer/CreateOrUpdateCustomer")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Object.class);
    }

    // ==================== Lookups APIs ====================

    /**
     * Get all items for a combobox based on the lookup type.
     */
    public Mono<Object> getAllItemsComboboxItems(
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
                .bodyToMono(Object.class);
    }

    // ==================== File Upload APIs ====================

    /**
     * Upload a base64 encoded file.
     */
    public Mono<Object> uploadBase64File(Object request) {
        return renteyServiceWebClient.post()
                .uri(basePath + "/FileUpload/UploadBase64File")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Object.class);
    }
}

