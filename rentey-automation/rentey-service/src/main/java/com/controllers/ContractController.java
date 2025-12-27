package com.controllers;

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
import com.services.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.controllers.ApiPaths.*;

@RestController
@RequestMapping(path = BASE_PATH)
public class ContractController {

    @Autowired
    private ContractService contractService;

    /**
     * Get countries phone information.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param typeId The type ID for filtering countries (required).
     * @param includeInActive Whether to include inactive countries (default: false).
     * @param includeNotAssign Whether to include not assigned countries (default: false).
     * @return The response containing all countries phone information.
     */
    @GetMapping(path = COUNTRY_GET_COUNTRIES_PHONE, produces = "application/json")
    public GetCountriesPhoneResponseBean getCountriesPhone(
            @RequestParam(required = true) Integer typeId,
            @RequestParam(required = false, defaultValue = "false") Boolean includeInActive,
            @RequestParam(required = false, defaultValue = "false") Boolean includeNotAssign) {

        if (typeId == null) {
            throw new IllegalArgumentException("typeId parameter is required.");
        }

        return contractService.getCountriesPhone();
    }

    /**
     * Get extras names excluded from booking payment details.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @return The response containing a list of extras names excluded from booking payment details.
     */
    @GetMapping(path = CONTRACT_EXTRA_CONFIGURATION_GET_EXTRAS_NAMES_EXCLUDED, produces = "application/json")
    public GetExtrasNamesExcludedFromBookingPaymentDetailsResponseBean getExtrasNamesExcludedFromBookingPaymentDetails() {
        return contractService.getExtrasNamesExcludedFromBookingPaymentDetails();
    }

    /**
     * Get contract extra items.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
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
    @GetMapping(path = CONTRACT_EXTRA_CONFIGURATION_GET_CONTRACT_EXTRA_ITEMS, produces = "application/json")
    public GetContractExtraItemsResponseBean getContractExtraItems(
            @RequestParam(required = true) Integer branchId,
            @RequestParam(required = true) Integer categoryId,
            @RequestParam(required = true) Integer rentalRatesSchemaPeriodId,
            @RequestParam(required = true) Integer operationType,
            @RequestParam(required = true) Integer contractType,
            @RequestParam(required = true) Integer source,
            @RequestParam(required = false, defaultValue = "false") Boolean includeInactive,
            @RequestParam(required = true) Integer rentalTypeId) {

        if (branchId == null) {
            throw new IllegalArgumentException("branchId parameter is required.");
        }
        if (categoryId == null) {
            throw new IllegalArgumentException("categoryId parameter is required.");
        }
        if (rentalRatesSchemaPeriodId == null) {
            throw new IllegalArgumentException("rentalRatesSchemaPeriodId parameter is required.");
        }
        if (operationType == null) {
            throw new IllegalArgumentException("operationType parameter is required.");
        }
        if (contractType == null) {
            throw new IllegalArgumentException("contractType parameter is required.");
        }
        if (source == null) {
            throw new IllegalArgumentException("source parameter is required.");
        }
        if (rentalTypeId == null) {
            throw new IllegalArgumentException("rentalTypeId parameter is required.");
        }

        return contractService.getContractExtraItems(
                branchId, categoryId, rentalRatesSchemaPeriodId, operationType,
                contractType, source, includeInactive, rentalTypeId);
    }

    /**
     * Get open contract date inputs.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param pickupBranch The pickup branch ID (required).
     * @return The response containing open contract date inputs.
     */
    @GetMapping(path = CONTRACT_GET_OPEN_CONTRACT_DATE_INPUTS, produces = "application/json")
    public GetOpenContractDateInputsResponseBean getOpenContractDateInputs(
            @RequestParam(required = true) Integer pickupBranch) {

        if (pickupBranch == null) {
            throw new IllegalArgumentException("pickupBranch parameter is required.");
        }

        return contractService.getOpenContractDateInputs(pickupBranch);
    }

    /**
     * Get matching offers for contract.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
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
    @GetMapping(path = CONTRACT_GET_MATCHING_OFFERS, produces = "application/json")
    public GetMatchingOffersResponseBean getMatchingOffers(
            @RequestParam(required = true) Long customerId,
            @RequestParam(required = true) Integer vehicleId,
            @RequestParam(required = true) Integer pickupBranchId,
            @RequestParam(required = true) String contractDateTimeRangeStart,
            @RequestParam(required = true) String contractDateTimeRangeEnd,
            @RequestParam(required = true) Integer vehicleModelId,
            @RequestParam(required = true) Integer vehicleYear,
            @RequestParam(required = false, defaultValue = "false") Boolean isBookingOffers,
            @RequestParam(required = false, defaultValue = "false") Boolean excludeZeroBenefits,
            @RequestParam(required = false) String contractDateTimeRangeDuration,
            @RequestParam(required = false, defaultValue = "false") Boolean isExecuteBooking) {

        if (customerId == null) {
            throw new IllegalArgumentException("customerId parameter is required.");
        }
        if (vehicleId == null) {
            throw new IllegalArgumentException("vehicleId parameter is required.");
        }
        if (pickupBranchId == null) {
            throw new IllegalArgumentException("pickupBranchId parameter is required.");
        }
        if (contractDateTimeRangeStart == null || contractDateTimeRangeStart.isEmpty()) {
            throw new IllegalArgumentException("contractDateTimeRangeStart parameter is required.");
        }
        if (contractDateTimeRangeEnd == null || contractDateTimeRangeEnd.isEmpty()) {
            throw new IllegalArgumentException("contractDateTimeRangeEnd parameter is required.");
        }
        if (vehicleModelId == null) {
            throw new IllegalArgumentException("vehicleModelId parameter is required.");
        }
        if (vehicleYear == null) {
            throw new IllegalArgumentException("vehicleYear parameter is required.");
        }

        return contractService.getMatchingOffers(
                customerId, vehicleId, pickupBranchId, contractDateTimeRangeStart,
                contractDateTimeRangeEnd, vehicleModelId, vehicleYear, isBookingOffers,
                excludeZeroBenefits, contractDateTimeRangeDuration, isExecuteBooking);
    }

    /**
     * Get all applicable driver authorization combobox items.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param vehicleId The vehicle ID (required).
     * @param customerId The customer ID (required).
     * @param contractMode The contract mode (required).
     * @return The response containing driver authorization combobox items.
     */
    @GetMapping(path = DRIVER_AUTHORIZATION_GET_ALL_APPLICABLE_DRIVER_AUTHORIZATION_COMBOBOX_ITEMS, produces = "application/json")
    public GetAllApplicableDriverAuthorizationComboboxItemsResponseBean getAllApplicableDriverAuthorizationComboboxItems(
            @RequestParam(required = true) Integer vehicleId,
            @RequestParam(required = true) Long customerId,
            @RequestParam(required = true) Integer contractMode) {

        if (vehicleId == null) {
            throw new IllegalArgumentException("vehicleId parameter is required.");
        }
        if (customerId == null) {
            throw new IllegalArgumentException("customerId parameter is required.");
        }
        if (contractMode == null) {
            throw new IllegalArgumentException("contractMode parameter is required.");
        }

        return contractService.getAllApplicableDriverAuthorizationComboboxItems(vehicleId, customerId, contractMode);
    }

    /**
     * Cancel driver authorization if cancellation is required.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param enableAuthorization Whether to enable authorization (required).
     * @param request The request containing driver authorization cancellation information.
     * @return The response containing validation results.
     */
    @PostMapping(path = DRIVER_AUTHORIZATION_CANCEL_DRIVER_AUTHORIZATION_IF_CANCELLATION_REQUIRED, consumes = "application/json", produces = "application/json")
    public CancelDriverAuthorizationIfCancellationRequiredResponseBean cancelDriverAuthorizationIfCancellationRequired(
            @RequestParam(required = true) Boolean enableAuthorization,
            @RequestBody(required = true) CancelDriverAuthorizationIfCancellationRequiredRequestBean request) {

        if (enableAuthorization == null) {
            throw new IllegalArgumentException("enableAuthorization parameter is required.");
        }
        if (request == null) {
            throw new IllegalArgumentException("Request body is required and cannot be null.");
        }

        return contractService.cancelDriverAuthorizationIfCancellationRequired(enableAuthorization, request);
    }

    /**
     * Authorize driver.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param request The request containing driver authorization information.
     * @return The response containing authorization results.
     */
    @PostMapping(path = DRIVER_AUTHORIZATION_AUTHORIZE_DRIVER, consumes = "application/json", produces = "application/json")
    public AuthorizeDriverResponseBean authorizeDriver(
            @RequestBody(required = true) AuthorizeDriverRequestBean request) {

        if (request == null) {
            throw new IllegalArgumentException("Request body is required and cannot be null.");
        }

        return contractService.authorizeDriver(request);
    }

    /**
     * Calculate billing information for contract.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param request The request containing billing calculation parameters.
     * @return The response containing calculated billing information.
     */
    @PostMapping(path = CONTRACT_CALCULATE_BILLING_INFORMATION, consumes = "application/json", produces = "application/json")
    public CalculateBillingInformationResponseBean calculateBillingInformation(
            @RequestBody(required = true) CalculateBillingInformationRequestBean request) {

        if (request == null) {
            throw new IllegalArgumentException("Request body is required and cannot be null.");
        }

        return contractService.calculateBillingInformation(request);
    }

    /**
     * Block vehicle usage for long period.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param vehicleId The vehicle ID (required).
     * @param branchId The branch ID (required).
     * @return The response containing the blocking operation result (UUID).
     */
    @PostMapping(path = RENTAL_VEHICLE_BLOCK_VEHICLE_USAGE_FOR_LONG_PERIOD, produces = "application/json")
    public AbpResponseBean blockVehicleUsageForLongPeriod(
            @RequestParam(required = true) Integer vehicleId,
            @RequestParam(required = true) Integer branchId) {

        if (vehicleId == null) {
            throw new IllegalArgumentException("vehicleId parameter is required.");
        }
        if (branchId == null) {
            throw new IllegalArgumentException("branchId parameter is required.");
        }

        return contractService.blockVehicleUsageForLongPeriod(vehicleId, branchId);
    }

    /**
     * Get integrated loyalties from Loyalty API.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     * Note: This API uses a different base path (loyaltyapigw) instead of the standard webapigw.
     *
     * @return The response containing a list of integrated loyalty programs.
     */
    @GetMapping(path = LOYALTY_GET_INTEGRATED_LOYALTIES_FROM_LOYALTY_API, produces = "application/json")
    public List<GetIntegratedLoyaltiesFromLoyaltyApiResponseBean> getIntegratedLoyaltiesFromLoyaltyApi() {
        return contractService.getIntegratedLoyaltiesFromLoyaltyApi();
    }

    /**
     * Get external loyalties configurations items from Loyalty API.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     * Note: This API uses a different base path (loyaltyapigw) instead of the standard webapigw.
     *
     * @param includeInActive Whether to include inactive items (default: false).
     * @return The response containing external loyalty configuration items.
     */
    @GetMapping(path = LOYALTY_GET_EXTERNAL_LOYALTIES_CONFIGURATIONS_ITEMS_FROM_LOYALTY_API, produces = "application/json")
    public GetExternalLoyaltiesConfigurationsItemsFromLoyaltyApiResponseBean getExternalLoyaltiesConfigurationsItemsFromLoyaltyApi(
            @RequestParam(required = false, defaultValue = "false") Boolean includeInActive) {
        return contractService.getExternalLoyaltiesConfigurationsItemsFromLoyaltyApi(includeInActive);
    }

    /**
     * Get external loyalties with allow redeem combobox from Loyalty API.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     * Note: This API uses a different base path (loyaltyapigw) instead of the standard webapigw.
     *
     * @param customerId The customer ID (required).
     * @param branchId The branch ID (required).
     * @return The response containing a list of external loyalties with allow redeem combobox items.
     */
    @GetMapping(path = LOYALTY_GET_EXTERNAL_LOYALTIES_WITH_ALLOW_REDEEM_COMBOBOX_FROM_LOYALTY_API, produces = "application/json")
    public List<GetExternalLoyaltiesWithAllowRedeemComboboxFromLoyaltyApiResponseBean> getExternalLoyaltiesWithAllowRedeemComboboxFromLoyaltyApi(
            @RequestParam(required = true) Long customerId,
            @RequestParam(required = true) Integer branchId) {

        if (customerId == null) {
            throw new IllegalArgumentException("customerId parameter is required.");
        }
        if (branchId == null) {
            throw new IllegalArgumentException("branchId parameter is required.");
        }

        return contractService.getExternalLoyaltiesWithAllowRedeemComboboxFromLoyaltyApi(customerId, branchId);
    }

    /**
     * Get external loyalties with allow earn combobox from Loyalty API.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param customerId The customer ID (required).
     * @param branchId The branch ID (required).
     * @return The response containing external loyalties with allow earn combobox items.
     */
    @GetMapping(path = LOYALTY_GET_EXTERNAL_LOYALTIES_WITH_ALLOW_EARN_COMBOBOX_FROM_LOYALTY_API, produces = "application/json")
    public GetExternalLoyaltiesWithAllowEarnComboboxResponseBean getExternalLoyaltiesWithAllowEarnComboboxFromLoyaltyApi(
            @RequestParam(required = true) Long customerId,
            @RequestParam(required = true) Integer branchId) {

        if (customerId == null) {
            throw new IllegalArgumentException("customerId parameter is required.");
        }
        if (branchId == null) {
            throw new IllegalArgumentException("branchId parameter is required.");
        }

        return contractService.getExternalLoyaltiesWithAllowEarnComboboxFromLoyaltyApi(customerId, branchId);
    }

    /**
     * Validate contract information.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param request The request containing contract validation information.
     * @return The response containing validation results.
     */
    @PostMapping(path = CONTRACT_VALIDATE_CONTRACT_INFO, consumes = "application/json", produces = "application/json")
    public AbpResponseBean validateContractInfo(
            @RequestBody(required = true) ValidateContractInfoRequestBean request) {

        if (request == null) {
            throw new IllegalArgumentException("Request body is required and cannot be null.");
        }

        return contractService.validateContractInfo(request);
    }

    /**
     * Validate prevent renting restriction for contract.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param customerId The customer ID (required).
     * @param pickupBranchId The pickup branch ID (required).
     * @param vehicleModelId The vehicle model ID (required).
     * @param pickupDate The pickup date (required, format: yyyy-MM-dd HH:mm:ss).
     * @return The response containing validation results.
     */
    @PostMapping(path = CONTRACT_VALIDATE_PREVENT_RENTING_RESTRICTION, produces = "application/json")
    public AbpResponseBean validatePreventRentingRestriction(
            @RequestParam(required = true) Long customerId,
            @RequestParam(required = true) Integer pickupBranchId,
            @RequestParam(required = true) Integer vehicleModelId,
            @RequestParam(required = true) String pickupDate) {

        if (customerId == null) {
            throw new IllegalArgumentException("customerId parameter is required.");
        }
        if (pickupBranchId == null) {
            throw new IllegalArgumentException("pickupBranchId parameter is required.");
        }
        if (vehicleModelId == null) {
            throw new IllegalArgumentException("vehicleModelId parameter is required.");
        }
        if (pickupDate == null || pickupDate.isEmpty()) {
            throw new IllegalArgumentException("pickupDate parameter is required.");
        }

        return contractService.validatePreventRentingRestriction(customerId, pickupBranchId, vehicleModelId, pickupDate);
    }
}

