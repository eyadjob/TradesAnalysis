package com.services;

import com.beans.AbpResponseBean;
import com.beans.TenantAndCountrySettingsRequestBean;
import com.beans.UpdateAllSettingsRequestBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.beans.GetOperationalCountriesResponseBean;

@Service
public class SettingsService {

    private final WebClient settingsWebClient;

    public SettingsService(
            @Qualifier("settingsWebClient") WebClient settingsWebClient) {
        this.settingsWebClient = settingsWebClient;
    }

    public AbpResponseBean updateAllSettings(UpdateAllSettingsRequestBean request) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.post()
                .uri("/webapigw/api/services/app/TenantSettings/UpdateAllSettings")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AbpResponseBean.class)
                .block();
    }

    public AbpResponseBean changeTenantSettings(Integer countryId, TenantAndCountrySettingsRequestBean request) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/webapigw/api/services/app/GeoSettings/ChangeTenantSettings")
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
                        .path("/webapigw/api/services/app/GeoSettings/UpdateCountrySettings")
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
                        .path("/webapigw/api/services/app/GeoSettings/ChangeBranchSettings")
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
    public GetOperationalCountriesResponseBean getOperationalCountries() {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri("/webapigw/api/services/app/Country/GetOperationalCountries")
                .retrieve()
                .bodyToMono(GetOperationalCountriesResponseBean.class)
                .block();
    }
}

