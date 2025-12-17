package com.services;

import com.annotation.LogExecutionTime;
import com.beans.contract.GetContractExtraItemsResponseBean;
import com.beans.contract.GetExtrasNamesExcludedFromBookingPaymentDetailsResponseBean;
import com.beans.contract.GetOpenContractDateInputsResponseBean;
import com.beans.customer.GetCountriesPhoneResponseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

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
}

