package com.services;

import com.annotation.LogExecutionTime;
import com.beans.booking.GetCreateBookingDateInputsResponseBean;
import com.beans.customer.GetCustomerContractInformationByNameResponseBean;
import com.beans.loyalty.GetAllExternalLoyaltiesConfigurationsItemsResponseBean;
import com.beans.loyalty.GetExternalLoyaltiesWithAllowRedeemComboboxResponseBean;
import com.beans.loyalty.GetIntegratedLoyaltiesResponseBean;
import com.beans.validation.IsValidPhoneResponseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Service for interacting with booking-related APIs.
 */
@Service
public class BookingService {

    @Autowired
    @Qualifier("settingsWebClient")
    private WebClient settingsWebClient;

    @Autowired
    @Qualifier("apiBasePath")
    private String apiBasePath;

    @Autowired
    @Qualifier("apiBasePathWithoutService")
    private String apiBasePathWithoutService;

    /**
     * Get create booking date inputs.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param countryId The country ID for which to get the booking date inputs (required).
     * @return The response containing create booking date inputs.
     */
    @Cacheable(cacheNames = "createBookingDateInputsCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public GetCreateBookingDateInputsResponseBean getCreateBookingDateInputs(Integer countryId) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(apiBasePath + "/Booking/GetCreateBookingDateInputs")
                        .queryParam("countryId", countryId)
                        .build())
                .retrieve()
                .bodyToMono(GetCreateBookingDateInputsResponseBean.class)
                .block();
    }

    /**
     * Validate phone number.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param phoneNumber The phone number to validate (required).
     * @param phoneCode The phone code (country code) to validate (required).
     * @return The response containing the validation result (true if valid, false otherwise).
     */
    @Cacheable(cacheNames = "isValidPhoneCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public IsValidPhoneResponseBean isValidPhone(String phoneNumber, String phoneCode) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(apiBasePathWithoutService + "/ValidatePhone/IsValid")
                        .queryParam("phoneNumber", phoneNumber)
                        .queryParam("phoneCode", phoneCode)
                        .build())
                .retrieve()
                .bodyToMono(IsValidPhoneResponseBean.class)
                .block();
    }

    /**
     * Get customer contract information by name.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param customerName The customer name to search for (required).
     * @return The response containing customer contract information matching the name.
     */
    @Cacheable(cacheNames = "customerContractInformationByNameCache", keyGenerator = "AutoKeyGenerator")
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
     * Get all external loyalties configurations items.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param includeInActive Whether to include inactive items (default: false).
     * @return The response containing all external loyalties configurations items.
     */
    @Cacheable(cacheNames = "allExternalLoyaltiesConfigurationsItemsCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public GetAllExternalLoyaltiesConfigurationsItemsResponseBean getAllExternalLoyaltiesConfigurationsItems(Boolean includeInActive) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder
                            .path(apiBasePath + "/ExternalLoyaltyConfiguration/GetAllExternalLoyaltiesConfigurationsItems");
                    
                    if (includeInActive != null) {
                        builder.queryParam("includeInActive", includeInActive);
                    }
                    
                    return builder.build();
                })
                .retrieve()
                .bodyToMono(GetAllExternalLoyaltiesConfigurationsItemsResponseBean.class)
                .block();
    }

    /**
     * Get integrated loyalties.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @return The response containing all integrated loyalties.
     */
    @Cacheable(cacheNames = "integratedLoyaltiesCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public GetIntegratedLoyaltiesResponseBean getIntegratedLoyalties() {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(apiBasePath + "/ExternalLoyaltyConfiguration/GetIntegratedLoyalties")
                .retrieve()
                .bodyToMono(GetIntegratedLoyaltiesResponseBean.class)
                .block();
    }

    /**
     * Get external loyalties with allow redeem combobox.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param customerId The customer ID (required).
     * @param branchId The branch ID (required).
     * @return The response containing external loyalties with allow redeem combobox items.
     */
    @Cacheable(cacheNames = "externalLoyaltiesWithAllowRedeemComboboxCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public GetExternalLoyaltiesWithAllowRedeemComboboxResponseBean getExternalLoyaltiesWithAllowRedeemCombobox(Integer customerId, Integer branchId) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(apiBasePath + "/CustomerMembership/GetExternalLoyaltiesWithAllowRedeemCombobox")
                        .queryParam("customerId", customerId)
                        .queryParam("branchId", branchId)
                        .build())
                .retrieve()
                .bodyToMono(GetExternalLoyaltiesWithAllowRedeemComboboxResponseBean.class)
                .block();
    }
}

