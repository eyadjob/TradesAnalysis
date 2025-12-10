package com.services;

import com.beans.general.GetAllItemsComboboxItemsResponseBean;
import com.beans.country.GetCountryCurrencyInfoResponseBean;
import com.beans.country.GetCurrenciesForComboboxResponseBean;
import com.beans.user.GetUserBranchesForComboboxResponseBean;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CountryService {

    @Autowired
    @Qualifier("settingsWebClient")
    private WebClient settingsWebClient;

    @Autowired
    @Qualifier("apiBasePath")
    private String apiBasePath;

    /**
     * Get country currency information by country ID.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param countryId The country ID for which to get the currency information.
     * @return The response containing the country currency information.
     */
    @Cacheable(cacheNames = "countryCurrencyInfo", keyGenerator = "AutoKeyGenerator")
    public GetCountryCurrencyInfoResponseBean getCountryCurrencyInfo(Integer countryId) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(apiBasePath + "/Currency/GetCountryCurrencyInfo")
                        .queryParam("countryId", countryId)
                        .build())
                .retrieve()
                .bodyToMono(GetCountryCurrencyInfoResponseBean.class)
                .block();
    }

    /**
     * Get user branches for combobox.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param countryId The country ID for which to get the branches.
     * @param includeInActive Whether to include inactive branches.
     * @param includeAll Whether to include all branches.
     * @param filterTypes List of filter types to apply.
     * @return The response containing all user branches for combobox.
     */
    @Cacheable(cacheNames = "userBranchesForCombobox", keyGenerator = "AutoKeyGenerator")
    public GetUserBranchesForComboboxResponseBean getUserBranchesForCombobox(
            Integer countryId, Boolean includeInActive,
            Boolean includeAll,
            List<Integer> filterTypes) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder
                            .path(apiBasePath + "/Branch/GetUserBranchesForCombobox");
                    
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
                .bodyToMono(GetUserBranchesForComboboxResponseBean.class)
                .block();
    }

    public GetUserBranchesForComboboxResponseBean getUserBranchesForCombobox(int countryId,List<Integer> filterTypes) {
        return getUserBranchesForCombobox(countryId,false,false,filterTypes);
    }

    public String getBranchIdByName(GetUserBranchesForComboboxResponseBean userBranchesForComboboxResponseBean, String branchName) {
        return userBranchesForComboboxResponseBean.result().items().stream().filter(userBranches -> userBranches.displayText().equals(branchName))
                .findFirst().map(userBranches -> String.valueOf(userBranches.value())).orElse("-1");
    }

    /**
     * Get countries for combobox.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     * Results are cached for 2 hours.
     *
     * @param includeInActive Whether to include inactive countries (default: false).
     * @param includeNotAssign Whether to include "Not assigned" option (default: true).
     * @return The response containing all countries for combobox.
     */
    @Cacheable(cacheNames = "countriesForCombobox", keyGenerator = "AutoKeyGenerator")
    public GetAllItemsComboboxItemsResponseBean getCountriesForCombobox(
            Boolean includeInActive,
            Boolean includeNotAssign) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder
                            .path(apiBasePath + "/Country/GetCountriesForCombobox");
                    
                    if (includeInActive != null) {
                        builder.queryParam("includeInActive", includeInActive);
                    }
                    if (includeNotAssign != null) {
                        builder.queryParam("includeNotAssign", includeNotAssign);
                    }
                    
                    return builder.build();
                })
                .retrieve()
                .bodyToMono(GetAllItemsComboboxItemsResponseBean.class)
                .block();
    }

    /**
     * Get currencies for combobox.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param includeInActive Whether to include inactive currencies (default: false).
     * @return The response containing all currencies for combobox.
     */
    @Cacheable(cacheNames = "currenciesForCombobox", keyGenerator = "AutoKeyGenerator")
    public GetCurrenciesForComboboxResponseBean getCurrenciesForCombobox(Boolean includeInActive) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder
                            .path(apiBasePath + "/Currency/GetCurrenciesForCombobox");
                    
                    if (includeInActive != null) {
                        builder.queryParam("includeInActive", includeInActive);
                    }
                    
                    return builder.build();
                })
                .retrieve()
                .bodyToMono(GetCurrenciesForComboboxResponseBean.class)
                .block();
    }
}

