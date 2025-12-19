package com.services;

import com.annotation.LogExecutionTime;
import com.beans.booking.CalculateBillingInformationRequestBean;
import com.beans.booking.CalculateBillingInformationResponseBean;
import com.beans.booking.CreateBookingRequestBean;
import com.beans.booking.CreateBookingResponseBean;
import com.builders.CreateBookingRequestBuilder;
import com.beans.booking.GetBestRentalRateForModelResponseBean;
import com.beans.booking.GetBranchAvailableModelsForBookingComboboxItemsRequestBean;
import com.beans.booking.GetBranchAvailableModelsForBookingComboboxItemsResponseBean;
import com.beans.booking.GetCreateBookingDateInputsResponseBean;
import com.beans.booking.ValidateDurationAndLocationsRequestBean;
import com.beans.booking.ValidateDurationAndLocationsResponseBean;
import com.beans.contract.GetExtrasNamesExcludedFromBookingPaymentDetailsResponseBean;
import com.beans.country.GetCountrySettingsResponseBean;
import com.beans.customer.CreateOrUpdateCustomerResponseBean;
import com.beans.customer.GetCountriesPhoneResponseBean;
import com.beans.customer.GetCustomerContractInformationByNameResponseBean;
import com.beans.lookups.GetItemsByTypeResponseBean;
import com.beans.loyalty.GetAllExternalLoyaltiesConfigurationsItemsResponseBean;
import com.beans.loyalty.GetExternalLoyaltiesWithAllowRedeemComboboxResponseBean;
import com.beans.loyalty.GetIntegratedLoyaltiesResponseBean;
import com.beans.validation.IsValidPhoneResponseBean;
import com.enums.LookupTypes;
import com.util.PropertyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Service for interacting with booking-related APIs.
 */
@Service
public class BookingService {

    @Autowired
    @Qualifier("settingsWebClient")
    private WebClient settingsWebClient;

    private static final Map<String, String> userDefinedVariables = PropertyManager.loadPropertyFileIntoMap("user-defined-variables.properties");

    @Autowired
    @Qualifier("apiBasePath")
    private String apiBasePath;

    @Autowired
    @Qualifier("apiBasePathWithoutService")
    private String apiBasePathWithoutService;

    @Autowired
    CountryService countryService;

    @Autowired
    LookupsService lookupsService;

    @Autowired
    CustomerService customerService;

    @Autowired
    VehicleService vehicleService;

    @Autowired
    VehicleOperationsService vehicleOperationsService;

    @Autowired
    ContractService contractService;

    @Autowired
    CustomerOperationsService customerOperationsService;

    @Autowired
    SettingsService settingsService;



    /**
     * Get create booking date inputs.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param countryId The country ID (required).
     * @return The response containing create booking date inputs.
     */
    @Cacheable(cacheNames = "createBookingDateInputsCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public GetCreateBookingDateInputsResponseBean getCreateBookingDateInputs(Integer countryId) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(apiBasePath + "/Booking/GetCreateBookingDateInputs")
                .retrieve()
                .bodyToMono(GetCreateBookingDateInputsResponseBean.class)
                .block();
    }

    /**
     * Get customer contract information by name.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param customerName The customer name (required).
     * @return The response containing customer contract information.
     */
    @LogExecutionTime
    public GetCustomerContractInformationByNameResponseBean getCustomerContractInformationByName(String customerName) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(apiBasePath + "/Customer/GetCustomerContractInformationByName")
                        .queryParam("customerName", customerName)
                        .build())
                .retrieve()
                .bodyToMono(GetCustomerContractInformationByNameResponseBean.class)
                .block();
    }

    /**
     * Get best rental rate for model.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param countryId The country ID (required).
     * @param branchId The branch ID (required).
     * @param modelId The model ID (required).
     * @param year The year (required).
     * @param pickupDate The pickup date (required, format: yyyy-MM-dd HH:mm:ss).
     * @param dropoffDate The dropoff date (required, format: yyyy-MM-dd HH:mm:ss).
     * @return The response containing the best rental rate for the model.
     */
    @Cacheable(cacheNames = "bestRentalRateForModelCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public GetBestRentalRateForModelResponseBean getBestRentalRateForModel(
            Integer countryId,
            Integer branchId,
            Integer modelId,
            Integer year,
            String pickupDate,
            String dropoffDate) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(apiBasePath + "/RentalVehicle/GetBestRentalRateForModel")
                        .queryParam("countryId", countryId)
                        .queryParam("branchId", branchId)
                        .queryParam("modelId", modelId)
                        .queryParam("year", year)
                        .queryParam("pickupDate", pickupDate)
                        .queryParam("dropoffDate", dropoffDate)
                        .build())
                .retrieve()
                .bodyToMono(GetBestRentalRateForModelResponseBean.class)
                .block();
    }

    /**
     * Calculate billing information.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param request The request containing booking information for billing calculation.
     * @return The response containing calculated billing information.
     */
    @LogExecutionTime
    public CalculateBillingInformationResponseBean calculateBillingInformation(CalculateBillingInformationRequestBean request) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.post()
                .uri(apiBasePath + "/Booking/CalculateBillingInformation")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(CalculateBillingInformationResponseBean.class)
                .block();
    }

    /**
     * Create booking.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param request The request containing booking information to create a booking.
     * @return The response containing the created booking information.
     */
    @LogExecutionTime
    public CreateBookingResponseBean createBooking(CreateBookingRequestBean request) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.post()
                .uri(apiBasePath + "/CreateBooking/CreateBooking")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(CreateBookingResponseBean.class)
                .block();
    }

    /**
     * Validate duration and locations.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param request The request containing booking information for validation.
     * @param pickupDate The pickup date (required, format: yyyy-MM-dd HH:mm:ss).
     * @param dropoffDate The dropoff date (required, format: yyyy-MM-dd HH:mm:ss).
     * @return The response containing validation results.
     */
    @LogExecutionTime
    public ValidateDurationAndLocationsResponseBean validateDurationAndLocations(
            ValidateDurationAndLocationsRequestBean request,
            String pickupDate,
            String dropoffDate) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(apiBasePath + "/Booking/ValidateDurationANDLocations")
                        .queryParam("pickupDate", pickupDate)
                        .queryParam("dropOffDate", dropoffDate)
                        .build())
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ValidateDurationAndLocationsResponseBean.class)
                .block();
    }

    /**
     * Get branch available models for booking combobox items.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param request The request containing booking information.
     * @return The response containing available models for booking combobox items.
     */
    @Cacheable(cacheNames = "branchAvailableModelsForBookingComboboxItemsCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public GetBranchAvailableModelsForBookingComboboxItemsResponseBean getBranchAvailableModelsForBookingComboboxItems(
            GetBranchAvailableModelsForBookingComboboxItemsRequestBean request) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.post()
                .uri(apiBasePath + "/RentalVehicle/GetBranchAvailableModelsForBookingComboboxItems")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(GetBranchAvailableModelsForBookingComboboxItemsResponseBean.class)
                .block();
    }

    public String getModelIdByModelName(GetBranchAvailableModelsForBookingComboboxItemsRequestBean request, String modelName) {
        return getBranchAvailableModelsForBookingComboboxItems(request).result().items().stream()
                .flatMap(category -> category.bookingAvailableModelComboboxItems().stream())
                .filter(model -> model.displayText().equals(modelName))
                .findFirst()
                .map(model -> model.value())
                .orElse("-1");
    }
    public String getCategoryIddByCategoryName(GetBranchAvailableModelsForBookingComboboxItemsRequestBean request, String categoryName) {
        return getBranchAvailableModelsForBookingComboboxItems(request).result().items().stream().filter(m -> m.displayText().equals(categoryName)).findFirst().map(m -> m.value()).orElse("-1");
    }

    /**
     * Validate prevent renting restriction.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param customerId The customer ID (required).
     * @param pickupBranchId The pickup branch ID (required).
     * @param vehicleModelId The vehicle model ID (required).
     * @param pickupDate The pickup date (required, format: yyyy-MM-dd HH:mm:ss).
     * @return The response containing validation results.
     */
    @LogExecutionTime
    public ValidateDurationAndLocationsResponseBean validatePreventRentingRestriction(
            Long customerId,
            Integer pickupBranchId,
            Integer vehicleModelId,
            String pickupDate) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(apiBasePath + "/Booking/ValidatePreventRentingRestriction")
                        .queryParam("customerId", customerId)
                        .queryParam("pickupBranchId", pickupBranchId)
                        .queryParam("vehicleModelId", vehicleModelId)
                        .queryParam("pickupDate", pickupDate)
                        .build())
                .retrieve()
                .bodyToMono(ValidateDurationAndLocationsResponseBean.class)
                .block();
    }
}
