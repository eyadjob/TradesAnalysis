package com.controllers;

import com.beans.contract.GetContractExtraItemsResponseBean;
import com.beans.contract.GetExtrasNamesExcludedFromBookingPaymentDetailsResponseBean;
import com.beans.contract.GetExternalLoyaltiesConfigurationsItemsFromLoyaltyApiResponseBean;
import com.beans.contract.GetExternalLoyaltiesWithAllowRedeemComboboxFromLoyaltyApiResponseBean;
import com.beans.contract.GetIntegratedLoyaltiesFromLoyaltyApiResponseBean;
import com.beans.contract.GetOpenContractDateInputsResponseBean;
import com.beans.customer.GetCountriesPhoneResponseBean;
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
}

