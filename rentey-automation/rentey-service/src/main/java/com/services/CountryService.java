package com.services;

import com.annotation.LogExecutionTime;
import com.beans.general.GetAllItemsComboboxItemsResponseBean;
import com.beans.country.GetCountryCurrencyInfoResponseBean;
import com.beans.country.GetCountrySettingsResponseBean;
import com.beans.country.GetCurrenciesForComboboxResponseBean;
import com.beans.setting.GetOperationalCountriesResponseBean;
import com.beans.user.GetUserBranchesForComboboxResponseBean;

import java.util.ArrayList;
import java.util.Arrays;
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

    @Autowired
    SettingsService settingsService;

    private GetOperationalCountriesResponseBean countriesResponseBean;

    /**
     * Get country currency information by country ID.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param countryId The country ID for which to get the currency information.
     * @return The response containing the country currency information.
     */
    @Cacheable(cacheNames = "countryCurrencyInfo", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
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
    @LogExecutionTime
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

    @Cacheable(cacheNames = "userBranchesForCombobox", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public GetUserBranchesForComboboxResponseBean getUserBranchesForCombobox(int countryId,List<Integer> filterTypes) {
        return getUserBranchesForCombobox(countryId,false,false,filterTypes);
    }

    @Cacheable(cacheNames = "userBranchesForCombobox", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public GetUserBranchesForComboboxResponseBean getUserBranchesForCombobox(int countryId) {
        return getUserBranchesForCombobox(countryId,false,false,null);
    }



    public String getBranchIdByName(GetUserBranchesForComboboxResponseBean userBranchesForComboboxResponseBean, String branchName) {
        return userBranchesForComboboxResponseBean.result().items().stream().filter(userBranches -> userBranches.displayText().equals(branchName))
                .findFirst().map(userBranches -> String.valueOf(userBranches.value())).orElse("-1");
    }


    public String getBranchIdByName(String countryId, String branchName) {
        GetUserBranchesForComboboxResponseBean  branchesForCombobox = getUserBranchesForCombobox(Integer.parseInt(countryId));
        return branchesForCombobox.result().items().stream().filter(userBranches -> userBranches.displayText().equals(branchName))
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
    @LogExecutionTime
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
    @LogExecutionTime
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

    /**
     * Get nationalities for combobox.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param includeInActive Whether to include inactive nationalities (default: false).
     * @return The response containing all nationalities for combobox.
     */
    @Cacheable(cacheNames = "nationalitiesForComboboxCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public GetAllItemsComboboxItemsResponseBean getNationalitiesForCombobox(Boolean includeInActive) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder
                            .path(apiBasePath + "/Country/GetNationalitiesForCombobox");
                    
                    if (includeInActive != null) {
                        builder.queryParam("includeInActive", includeInActive);
                    }
                    
                    return builder.build();
                })
                .retrieve()
                .bodyToMono(GetAllItemsComboboxItemsResponseBean.class)
                .block();
    }

    /**
     * Get branches countries combobox items.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @return The response containing all branches countries combobox items.
     */
    @Cacheable(cacheNames = "branchesCountriesComboboxItemsCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public GetAllItemsComboboxItemsResponseBean getBranchesCountriesComboboxItems() {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(apiBasePath + "/Branch/GetBranchesCountriesComboboxItems")
                .retrieve()
                .bodyToMono(GetAllItemsComboboxItemsResponseBean.class)
                .block();
    }

    public String getOperationalCountryIdFromName(String documentIssueCountry) {
        if (this.countriesResponseBean == null) {
            this.countriesResponseBean = settingsService.getOperationalCountries();
        }
            for (GetOperationalCountriesResponseBean.OperationalCountry country : countriesResponseBean.result()) {
                if (country.name().equals(documentIssueCountry)) {
                    return String.valueOf(country.id());
                }
        }
        return "-1";
    }

    /**
     * Get country settings by country ID and keys.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     * Results are cached for 2 hours.
     *
     * @param countryId The country ID for which to get the settings (required).
     * @param keys List of setting keys to retrieve (required).
     * @return The response containing the country settings as key-value pairs.
     */
    @Cacheable(cacheNames = "countrySettingsCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public GetCountrySettingsResponseBean getCountrySettings(Integer countryId, List<String> keys) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder
                            .path(apiBasePath + "/CountrySettings/GetSettings");
                    
                    if (countryId != null) {
                        builder.queryParam("countryId", countryId);
                    }
                    if (keys != null && !keys.isEmpty()) {
                        for (String key : keys) {
                            builder.queryParam("keys", key);
                        }
                    }
                    
                    return builder.build();
                })
                .retrieve()
                .bodyToMono(GetCountrySettingsResponseBean.class)
                .block();
    }

    public List<String> buildKeysForSettingsToGet(String settingKeys ) {
        return Arrays.stream(settingKeys.split("keys=")).map(k -> k.replace("&","")).toList();
    }
}




