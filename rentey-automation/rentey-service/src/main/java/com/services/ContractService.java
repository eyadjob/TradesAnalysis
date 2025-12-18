package com.services;

import com.annotation.LogExecutionTime;
import com.beans.contract.GetContractExtraItemsResponseBean;
import com.beans.contract.GetExtrasNamesExcludedFromBookingPaymentDetailsResponseBean;
import com.beans.contract.GetExternalLoyaltiesConfigurationsItemsFromLoyaltyApiResponseBean;
import com.beans.contract.GetExternalLoyaltiesWithAllowRedeemComboboxFromLoyaltyApiResponseBean;
import com.beans.contract.GetIntegratedLoyaltiesFromLoyaltyApiResponseBean;
import com.beans.contract.GetOpenContractDateInputsResponseBean;
import com.beans.customer.GetCountriesPhoneResponseBean;
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
}

