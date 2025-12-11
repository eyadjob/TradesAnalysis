package com.services;

import com.beans.general.AbpResponseBean;
import com.beans.setting.TenantAndCountrySettingsRequestBean;
import com.beans.setting.UpdateAllSettingsRequestBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.beans.setting.GetOperationalCountriesResponseBean;

@Service
public class SettingsService {

    @Autowired
    @Qualifier("settingsWebClient")
    private WebClient settingsWebClient;

    @Autowired
    @Qualifier("apiBasePath")
    private String apiBasePath;

    public AbpResponseBean updateAllSettings(UpdateAllSettingsRequestBean request) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.post()
                .uri(apiBasePath + "/TenantSettings/UpdateAllSettings")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AbpResponseBean.class)
                .block();
    }


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
    public GetOperationalCountriesResponseBean getOperationalCountries() {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(apiBasePath + "/Country/GetOperationalCountries")
                .retrieve()
                .bodyToMono(GetOperationalCountriesResponseBean.class)
                .block();
    }
}

