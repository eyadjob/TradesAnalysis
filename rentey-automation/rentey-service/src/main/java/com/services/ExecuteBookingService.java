package com.services;

import com.annotation.LogExecutionTime;
import com.beans.contract.ExecuteBookingRequestBean;
import com.beans.contract.ValidateCustomerResponseBean;
import com.beans.customer.IsCustomerEligibleForCustomerProvidersIntegrationResponseBean;
import com.beans.customer.SearchCustomerRequestBean;
import com.beans.general.AbpResponseBean;
import com.beans.vehicle.GetReadyVehiclesByCategoryAndModelRequestBean;
import com.beans.vehicle.GetReadyVehiclesByCategoryAndModelResponseBean;
import com.beans.vehicle.GetReadyVehiclesModelResponseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Service for interacting with execute booking-related APIs.
 */
@Service
public class ExecuteBookingService {

    @Autowired
    @Qualifier("settingsWebClient")
    private WebClient settingsWebClient;

    @Autowired
    @Qualifier("apiBasePath")
    private String apiBasePath;

    /**
     * Search customer by customer ID, phone number, or identity number.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param request The request containing customer ID, phone number, or identity number.
     * @return The response containing matching customer information.
     */
    @Cacheable(cacheNames = "searchCustomerCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public AbpResponseBean searchCustomer(SearchCustomerRequestBean request) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.post()
                .uri(apiBasePath + "/Customer/SearchCustomer")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AbpResponseBean.class)
                .block();
    }

    /**
     * Check if booking is allowed to be executed.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param id The booking ID (required, e.g., 66731).
     * @return The response containing the result indicating if the booking is allowed to be executed.
     */
    @LogExecutionTime
    public AbpResponseBean isAllowedToExecuteBooking(Long id) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(apiBasePath + "/Booking/IsAllowedToExecuteBooking")
                        .queryParam("id", id)
                        .build())
                .retrieve()
                .bodyToMono(AbpResponseBean.class)
                .block();
    }

    /**
     * Get lite customer by customer ID.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param id The customer ID (required, e.g., 303206).
     * @return The response containing lite customer information.
     */
    @Cacheable(cacheNames = "getLiteCustomerCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public AbpResponseBean getLiteCustomer(Long id) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(apiBasePath + "/Customer/GetLiteCustomer")
                        .queryParam("id", id)
                        .build())
                .retrieve()
                .bodyToMono(AbpResponseBean.class)
                .block();
    }

    /**
     * Get lite car model by model ID.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param modelId The car model ID (required, e.g., 291).
     * @return The response containing lite car model information.
     */
    @Cacheable(cacheNames = "getLiteCarModelCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public AbpResponseBean getLiteCarModel(Integer modelId) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(apiBasePath + "/CarModel/GetLiteCarModel")
                        .queryParam("modelId", modelId)
                        .build())
                .retrieve()
                .bodyToMono(AbpResponseBean.class)
                .block();
    }

    /**
     * Get ready vehicles model by branch ID, category ID, and mode ID.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param branchId The branch ID (required).
     * @param categoryId The category ID (required).
     * @param modeId The mode ID (required).
     * @return The response containing ready vehicles model information.
     */
    @Cacheable(cacheNames = "getReadyVehiclesModelCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public GetReadyVehiclesModelResponseBean getReadyVehiclesModel(Integer branchId, Integer categoryId, Integer modeId) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(apiBasePath + "/RentalVehicle/GetReadyVehiclesModel")
                        .queryParam("branchId", branchId)
                        .queryParam("categoryId", categoryId)
                        .queryParam("modeId", modeId)
                        .build())
                .retrieve()
                .bodyToMono(GetReadyVehiclesModelResponseBean.class)
                .block();
    }

    /**
     * Get ready vehicles by category and model.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param request The request containing category ID, model ID, branch ID, isBooking, and isMonthlyContract.
     * @return The response containing ready vehicles information.
     */
    @LogExecutionTime
    public GetReadyVehiclesByCategoryAndModelResponseBean getReadyVehiclesByCategoryAndModel(GetReadyVehiclesByCategoryAndModelRequestBean request) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.post()
                .uri(apiBasePath + "/RentalVehicle/GetReadyVehiclesByCategoryAndModel")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(GetReadyVehiclesByCategoryAndModelResponseBean.class)
                .block();
    }

    /**
     * Check if customer is eligible for customer providers integration.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param customerId The customer ID (required, e.g., 303206).
     * @return The response containing customer eligibility information.
     */
    @LogExecutionTime
    public IsCustomerEligibleForCustomerProvidersIntegrationResponseBean isCustomerEligibleForCustomerProvidersIntegration(Long customerId) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(apiBasePath + "/Customer/IsCustomerEligibleForCustomerProvidersIntegration")
                        .queryParam("customerId", customerId)
                        .build())
                .retrieve()
                .bodyToMono(IsCustomerEligibleForCustomerProvidersIntegrationResponseBean.class)
                .block();
    }

    /**
     * Validate customer for contract.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param customerId The customer ID (required, e.g., 303206).
     * @return The response containing customer validation information.
     */
    @LogExecutionTime
    public ValidateCustomerResponseBean validateCustomer(Long customerId) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(apiBasePath + "/Contract/ValidateCustomer")
                        .queryParam("customerId", customerId)
                        .build())
                .retrieve()
                .bodyToMono(ValidateCustomerResponseBean.class)
                .block();
    }

    /**
     * Execute booking.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param request The request containing booking execution information.
     * @return The response containing the execution result.
     */
    @LogExecutionTime
    public AbpResponseBean executeBooking(ExecuteBookingRequestBean request) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.post()
                .uri(apiBasePath + "/Contract/ExecuteBooking")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AbpResponseBean.class)
                .block();
    }
}

