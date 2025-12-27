package com.services;

import com.annotation.LogExecutionTime;
import com.beans.contract.CalculateBillingInformationRequestBean;
import com.beans.contract.CalculateBillingInformationResponseBean;
import com.beans.contract.GetContractExtraItemsResponseBean;
import com.beans.contract.GetExtrasNamesExcludedFromBookingPaymentDetailsResponseBean;
import com.beans.contract.GetExternalLoyaltiesConfigurationsItemsFromLoyaltyApiResponseBean;
import com.beans.contract.GetExternalLoyaltiesWithAllowEarnComboboxResponseBean;
import com.beans.contract.GetExternalLoyaltiesWithAllowRedeemComboboxFromLoyaltyApiResponseBean;
import com.beans.contract.GetIntegratedLoyaltiesFromLoyaltyApiResponseBean;
import com.beans.contract.GetMatchingOffersResponseBean;
import com.beans.contract.GetOpenContractDateInputsResponseBean;
import com.beans.contract.ValidateContractInfoRequestBean;
import com.beans.customer.GetCountriesPhoneResponseBean;
import com.beans.driver.AuthorizeDriverRequestBean;
import com.beans.driver.AuthorizeDriverResponseBean;
import com.beans.driver.AuthorizeDriverRequestBean;
import com.beans.driver.AuthorizeDriverResponseBean;
import com.beans.driver.CancelDriverAuthorizationIfCancellationRequiredRequestBean;
import com.beans.driver.CancelDriverAuthorizationIfCancellationRequiredResponseBean;
import com.beans.driver.GetAllApplicableDriverAuthorizationComboboxItemsResponseBean;
import com.beans.general.AbpResponseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class ContractService {

    @Autowired
    @Qualifier("settingsWebClient")
    private WebClient settingsWebClient;

    @Autowired
    @Qualifier("apiBasePath")
    private String apiBasePath;

    /**
     * Get countries phone information.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @return The response containing all countries phone information.
     */
    @Cacheable(cacheNames = "countriesPhoneCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public GetCountriesPhoneResponseBean getCountriesPhone(){
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(apiBasePath + "/Country/GetCountriesPhone")
                        .build())
                .retrieve()
                .bodyToMono(GetCountriesPhoneResponseBean.class)
                .block();
    }

    /**
     * Get extras names excluded from booking payment details.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @return The response containing a list of extras names excluded from booking payment details.
     */
    @Cacheable(cacheNames = "extrasNamesExcludedFromBookingPaymentDetailsCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public GetExtrasNamesExcludedFromBookingPaymentDetailsResponseBean getExtrasNamesExcludedFromBookingPaymentDetails() {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(apiBasePath + "/ContractExtraConfiguration/GetExtrasNamesExcludedFromBookingPaymentDetails")
                .retrieve()
                .bodyToMono(GetExtrasNamesExcludedFromBookingPaymentDetailsResponseBean.class)
                .block();
    }

    /**
     * Get contract extra items.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param branchId The branch ID (required).
     * @param categoryId The category ID (required).
     * @param rentalRatesSchemaPeriodId The rental rates schema period ID (required).
     * @param operationType The operation type (required).
     * @param contractType The contract type (required).
     * @param source The source (required).
     * @param includeInactive Whether to include inactive items (default: false).
     * @param rentalTypeId The rental type ID (required).
     * @return The response containing contract extra items.
     */
    @Cacheable(cacheNames = "contractExtraItemsCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public GetContractExtraItemsResponseBean getContractExtraItems(
            Integer branchId,
            Integer categoryId,
            Integer rentalRatesSchemaPeriodId,
            Integer operationType,
            Integer contractType,
            Integer source,
            Boolean includeInactive,
            Integer rentalTypeId) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder
                            .path(apiBasePath + "/ContractExtraConfiguration/GetContractExtraItems")
                            .queryParam("branchId", branchId)
                            .queryParam("categoryId", categoryId)
                            .queryParam("rentalRatesSchemaPeriodId", rentalRatesSchemaPeriodId)
                            .queryParam("operationType", operationType)
                            .queryParam("contractType", contractType)
                            .queryParam("source", source)
                            .queryParam("rentalTypeId", rentalTypeId);
                    
                    if (includeInactive != null) {
                        builder.queryParam("includeInactive", includeInactive);
                    }
                    
                    return builder.build();
                })
                .retrieve()
                .bodyToMono(GetContractExtraItemsResponseBean.class)
                .block();
    }

    /**
     * Get open contract date inputs.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param pickupBranch The pickup branch ID (required).
     * @return The response containing open contract date inputs.
     */
    @Cacheable(cacheNames = "openContractDateInputsCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public GetOpenContractDateInputsResponseBean getOpenContractDateInputs(Integer pickupBranch) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(apiBasePath + "/Contract/GetOpenContractDateInputs")
                        .queryParam("pickupBranch", pickupBranch)
                        .build())
                .retrieve()
                .bodyToMono(GetOpenContractDateInputsResponseBean.class)
                .block();
    }

    /**
     * Get integrated loyalties from Loyalty API.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     * This endpoint retrieves all integrated loyalty programs from the loyalty API gateway.
     * Results are cached for 2 hours.
     *
     * @return The response containing a list of integrated loyalty programs.
     */
    @Cacheable(cacheNames = "integratedLoyaltiesFromLoyaltyApiCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public List<GetIntegratedLoyaltiesFromLoyaltyApiResponseBean> getIntegratedLoyaltiesFromLoyaltyApi() {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        // Note: This API uses a different base path (loyaltyapigw instead of webapigw)
        return settingsWebClient.get()
                .uri("/loyaltyapigw/api/app/external-loyalty-configuration/integrated-loyalties")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<GetIntegratedLoyaltiesFromLoyaltyApiResponseBean>>() {})
                .block();
    }

    /**
     * Get external loyalties configurations items from Loyalty API.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     * This endpoint retrieves external loyalty configuration items from the loyalty API gateway.
     * Results are cached for 2 hours.
     *
     * @param includeInActive Whether to include inactive items (default: false).
     * @return The response containing external loyalty configuration items.
     */
    @Cacheable(cacheNames = "externalLoyaltiesConfigurationsItemsFromLoyaltyApiCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public GetExternalLoyaltiesConfigurationsItemsFromLoyaltyApiResponseBean getExternalLoyaltiesConfigurationsItemsFromLoyaltyApi(Boolean includeInActive) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        // Note: This API uses a different base path (loyaltyapigw instead of webapigw)
        return settingsWebClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder
                            .path("/loyaltyapigw/api/app/external-loyalty-configuration/external-loyalties-configurations-items");
                    
                    if (includeInActive != null) {
                        builder.queryParam("includeInActive", includeInActive);
                    }
                    
                    return builder.build();
                })
                .retrieve()
                .bodyToMono(GetExternalLoyaltiesConfigurationsItemsFromLoyaltyApiResponseBean.class)
                .block();
    }

    /**
     * Get external loyalties with allow redeem combobox from Loyalty API.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     * This endpoint retrieves external loyalties that allow redeem for a specific customer and branch.
     * Results are cached for 2 hours.
     *
     * @param customerId The customer ID (required).
     * @param branchId The branch ID (required).
     * @return The response containing a list of external loyalties with allow redeem combobox items.
     */
    @Cacheable(cacheNames = "externalLoyaltiesWithAllowRedeemComboboxFromLoyaltyApiCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public List<GetExternalLoyaltiesWithAllowRedeemComboboxFromLoyaltyApiResponseBean> getExternalLoyaltiesWithAllowRedeemComboboxFromLoyaltyApi(
            Long customerId,
            Integer branchId) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        // Note: This API uses a different base path (loyaltyapigw instead of webapigw)
        return settingsWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/loyaltyapigw/api/app/customer-membership/external-loyalties-with-allow-redeem-combobox")
                        .queryParam("customerId", customerId)
                        .queryParam("branchId", branchId)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<GetExternalLoyaltiesWithAllowRedeemComboboxFromLoyaltyApiResponseBean>>() {})
                .block();
    }

    /**
     * Get external loyalties with allow earn combobox from Loyalty API.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     * This endpoint retrieves external loyalties that allow earn for a specific customer and branch.
     * Results are cached for 2 hours.
     *
     * @param customerId The customer ID (required).
     * @param branchId The branch ID (required).
     * @return The response containing external loyalties with allow earn combobox items.
     */
    @Cacheable(cacheNames = "externalLoyaltiesWithAllowEarnComboboxFromLoyaltyApiCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public GetExternalLoyaltiesWithAllowEarnComboboxResponseBean getExternalLoyaltiesWithAllowEarnComboboxFromLoyaltyApi(
            Long customerId,
            Integer branchId) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        // Note: This API uses a different base path (loyaltyapigw instead of webapigw)
        return settingsWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/loyaltyapigw/api/app/customer-membership/external-loyalties-with-allow-earn-combobox")
                        .queryParam("customerId", customerId)
                        .queryParam("branchId", branchId)
                        .build())
                .retrieve()
                .bodyToMono(GetExternalLoyaltiesWithAllowEarnComboboxResponseBean.class)
                .block();
    }

    /**
     * Validate contract information.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param request The request containing contract validation information.
     * @return The response containing validation results.
     */
    @LogExecutionTime
    public AbpResponseBean validateContractInfo(ValidateContractInfoRequestBean request) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.post()
                .uri(apiBasePath + "/Contract/ValidateContractInfo")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AbpResponseBean.class)
                .block();
    }

    /**
     * Validate prevent renting restriction for contract.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param customerId The customer ID (required).
     * @param pickupBranchId The pickup branch ID (required).
     * @param vehicleModelId The vehicle model ID (required).
     * @param pickupDate The pickup date (required, format: yyyy-MM-dd HH:mm:ss).
     * @return The response containing validation results.
     */
    @LogExecutionTime
    public AbpResponseBean validatePreventRentingRestriction(
            Long customerId,
            Integer pickupBranchId,
            Integer vehicleModelId,
            String pickupDate) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(apiBasePath + "/Contract/ValidatePreventRentingRestriction")
                        .queryParam("customerId", customerId)
                        .queryParam("pickupBranchId", pickupBranchId)
                        .queryParam("vehicleModelId", vehicleModelId)
                        .queryParam("pickupDate", pickupDate)
                        .build())
                .retrieve()
                .bodyToMono(AbpResponseBean.class)
                .block();
    }

    /**
     * Get matching offers for contract.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param customerId The customer ID (required).
     * @param vehicleId The vehicle ID (required).
     * @param pickupBranchId The pickup branch ID (required).
     * @param contractDateTimeRangeStart The contract date time range start (required, format: yyyy-MM-dd HH:mm:ss).
     * @param contractDateTimeRangeEnd The contract date time range end (required, format: yyyy-MM-dd HH:mm:ss).
     * @param vehicleModelId The vehicle model ID (required).
     * @param vehicleYear The vehicle year (required).
     * @param isBookingOffers Whether to get booking offers (default: false).
     * @param excludeZeroBenefits Whether to exclude zero benefits (default: false).
     * @param contractDateTimeRangeDuration The contract date time range duration (optional).
     * @param isExecuteBooking Whether this is for executing booking (default: false).
     * @return The response containing matching offers.
     */
    @Cacheable(cacheNames = "getMatchingOffersCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public GetMatchingOffersResponseBean getMatchingOffers(
            Long customerId,
            Integer vehicleId,
            Integer pickupBranchId,
            String contractDateTimeRangeStart,
            String contractDateTimeRangeEnd,
            Integer vehicleModelId,
            Integer vehicleYear,
            Boolean isBookingOffers,
            Boolean excludeZeroBenefits,
            String contractDateTimeRangeDuration,
            Boolean isExecuteBooking) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder
                            .path(apiBasePath + "/Contract/GetMatchingOffers")
                            .queryParam("CustomerId", customerId)
                            .queryParam("VehicleId", vehicleId)
                            .queryParam("PickupBranchId", pickupBranchId)
                            .queryParam("ContractDateTimeRange.Start", contractDateTimeRangeStart)
                            .queryParam("ContractDateTimeRange.End", contractDateTimeRangeEnd)
                            .queryParam("VehicleModelId", vehicleModelId)
                            .queryParam("VehicleYear", vehicleYear);
                    
                    if (isBookingOffers != null) {
                        builder.queryParam("IsBookingOffers", isBookingOffers);
                    }
                    if (excludeZeroBenefits != null) {
                        builder.queryParam("ExcludeZeroBenefits", excludeZeroBenefits);
                    }
                    if (contractDateTimeRangeDuration != null) {
                        builder.queryParam("ContractDateTimeRange.Duration", contractDateTimeRangeDuration);
                    }
                    if (isExecuteBooking != null) {
                        builder.queryParam("IsExecuteBooking", isExecuteBooking);
                    }
                    
                    return builder.build();
                })
                .retrieve()
                .bodyToMono(GetMatchingOffersResponseBean.class)
                .block();
    }

    /**
     * Get all applicable driver authorization combobox items.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param vehicleId The vehicle ID (required).
     * @param customerId The customer ID (required).
     * @param contractMode The contract mode (required).
     * @return The response containing driver authorization combobox items.
     */
    @Cacheable(cacheNames = "getAllApplicableDriverAuthorizationComboboxItemsCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public GetAllApplicableDriverAuthorizationComboboxItemsResponseBean getAllApplicableDriverAuthorizationComboboxItems(
            Integer vehicleId,
            Long customerId,
            Integer contractMode) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(apiBasePath + "/DriverAuthorization/GetAllApplicableDriverAuthorizationComboboxItems")
                        .queryParam("vehicleId", vehicleId)
                        .queryParam("customerId", customerId)
                        .queryParam("contractMode", contractMode)
                        .build())
                .retrieve()
                .bodyToMono(GetAllApplicableDriverAuthorizationComboboxItemsResponseBean.class)
                .block();
    }

    /**
     * Cancel driver authorization if cancellation is required.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param enableAuthorization Whether to enable authorization (required).
     * @param request The request containing driver authorization cancellation information.
     * @return The response containing validation results.
     */
    @LogExecutionTime
    public CancelDriverAuthorizationIfCancellationRequiredResponseBean cancelDriverAuthorizationIfCancellationRequired(
            Boolean enableAuthorization,
            CancelDriverAuthorizationIfCancellationRequiredRequestBean request) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(apiBasePath + "/DriverAuthorization/CancelDriverAuthorizationIfCancellationRequired")
                        .queryParam("enableAuthorization", enableAuthorization)
                        .build())
                .bodyValue(request)
                .retrieve()
                .bodyToMono(CancelDriverAuthorizationIfCancellationRequiredResponseBean.class)
                .block();
    }

    /**
     * Authorize driver.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param request The request containing driver authorization information.
     * @return The response containing authorization results.
     */
    @LogExecutionTime
    public AuthorizeDriverResponseBean authorizeDriver(AuthorizeDriverRequestBean request) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.post()
                .uri(apiBasePath + "/DriverAuthorization/AuthorizeDriver")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AuthorizeDriverResponseBean.class)
                .block();
    }

    /**
     * Calculate billing information for contract.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param request The request containing billing calculation parameters.
     * @return The response containing calculated billing information.
     */
    @LogExecutionTime
    public CalculateBillingInformationResponseBean calculateBillingInformation(CalculateBillingInformationRequestBean request) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.post()
                .uri(apiBasePath + "/Contract/CalculateBillingInformation")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(CalculateBillingInformationResponseBean.class)
                .block();
    }

    /**
     * Block vehicle usage for long period.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param vehicleId The vehicle ID (required).
     * @param branchId The branch ID (required).
     * @return The response containing the blocking operation result (UUID).
     */
    @LogExecutionTime
    public AbpResponseBean blockVehicleUsageForLongPeriod(Integer vehicleId, Integer branchId) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(apiBasePath + "/RentalVehicle/BlockVehicleUsageForLongPeriod")
                        .queryParam("vehicleId", vehicleId)
                        .queryParam("branchId", branchId)
                        .build())
                .retrieve()
                .bodyToMono(AbpResponseBean.class)
                .block();
    }
}

