package com.services;

import com.annotation.LogExecutionTime;
import com.beans.general.AbpResponseBean;
import com.beans.setting.GetAllRentalRatesSchemasResponseBean;
import com.beans.setting.GetBranchSettingsResponseBean;
import com.beans.setting.GetOperationalCountriesResponseBean;
import com.beans.setting.TenantAndCountrySettingsRequestBean;
import com.beans.setting.UpdateAllSettingsRequestBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class SettingsService {

    @Autowired
    @Qualifier("settingsWebClient")
    private WebClient settingsWebClient;

    @Autowired
    @Qualifier("apiBasePath")
    private String apiBasePath;

    @LogExecutionTime
    public AbpResponseBean updateAllSettings(UpdateAllSettingsRequestBean request) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.post()
                .uri(apiBasePath + "/TenantSettings/UpdateAllSettings")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AbpResponseBean.class)
                .block();
    }

    @LogExecutionTime
    public AbpResponseBean changeTenantSettings(Integer countryId, TenantAndCountrySettingsRequestBean request) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(apiBasePath + "/GeoSettings/ChangeTenantSettings")
                        .queryParam("countryId", countryId)
                        .build())
                .bodyValue(request.settings())
                .retrieve()
                .bodyToMono(AbpResponseBean.class)
                .block();
    }

    @LogExecutionTime
    public AbpResponseBean updateCountrySettings(Integer countryId, TenantAndCountrySettingsRequestBean request) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(apiBasePath + "/GeoSettings/UpdateCountrySettings")
                        .queryParam("countryId", countryId)
                        .build())
                .bodyValue(request.settings())
                .retrieve()
                .bodyToMono(AbpResponseBean.class)
                .block();
    }

    @LogExecutionTime
    public AbpResponseBean changeBranchSettings(Integer countryId, Integer branchId, TenantAndCountrySettingsRequestBean request) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(apiBasePath + "/GeoSettings/ChangeBranchSettings")
                        .queryParam("countryId", countryId)
                        .queryParam("branchId", branchId)
                        .build())
                .bodyValue(request.settings())
                .retrieve()
                .bodyToMono(AbpResponseBean.class)
                .block();
    }

    /**
     * Get operational countries.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @return The response containing all operational countries.
     */
    @Cacheable(cacheNames = "operationalCountriesCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public GetOperationalCountriesResponseBean getOperationalCountries() {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(apiBasePath + "/Country/GetOperationalCountries")
                .retrieve()
                .bodyToMono(GetOperationalCountriesResponseBean.class)
                .block();
    }

    /**
     * Get all rental rates schemas.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     * This endpoint retrieves all rental rates schemas for a specific country.
     * Results are cached for 2 hours.
     *
     * @param countryId       The country ID (required).
     * @param includeInActive Whether to include inactive schemas (default: false).
     * @return The response containing all rental rates schemas.
     */
    @Cacheable(cacheNames = "allRentalRatesSchemasCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public GetAllRentalRatesSchemasResponseBean getAllRentalRatesSchemas(Integer countryId, Boolean includeInActive) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder
                            .path(apiBasePath + "/RentalRatesSchema/GetAllRentalRatesSchemas");
                    
                    if (includeInActive != null) {
                        builder.queryParam("includeInActive", includeInActive);
                    }
                    if (countryId != null) {
                        builder.queryParam("countryId", countryId);
                    }
                    
                    return builder.build();
                })
                .retrieve()
                .bodyToMono(GetAllRentalRatesSchemasResponseBean.class)
                .block();
    }

    public int getRentalSchemaIdByNameAndByPeriodTypeName(int countryId, String rentalSchemaName, String periodTypeName) {
       return getAllRentalRatesSchemas(countryId, false).result()
                .items()
                .stream()
                .filter(r -> r.name().equals(rentalSchemaName) && r.type().equals(periodTypeName))
                .map(r -> r.id())
                .findFirst()
                .orElse(-1);
    }

    /**
     * Get branch settings by country ID, branch ID, and keys.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     * This endpoint retrieves branch settings for a specific country and branch.
     * Results are cached for 2 hours.
     *
     * @param countryId The country ID (required).
     * @param branchId The branch ID (required).
     * @param keys List of setting keys to retrieve (required).
     * @return The response containing the branch settings as key-value pairs.
     */
    @Cacheable(cacheNames = "branchSettingsCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public GetBranchSettingsResponseBean getBranchSettings(Integer countryId, Integer branchId, List<String> keys) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder
                            .path(apiBasePath + "/BranchSettings/GetSettings")
                            .queryParam("countryId", countryId)
                            .queryParam("branchId", branchId);
                    
                    if (keys != null && !keys.isEmpty()) {
                        for (String key : keys) {
                            builder.queryParam("keys", key);
                        }
                    }
                    
                    return builder.build();
                })
                .retrieve()
                .bodyToMono(GetBranchSettingsResponseBean.class)
                .block();
    }
}

