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
    private final AuthorizationTokenService authorizationTokenService;

    public SettingsService(
            @Qualifier("settingsWebClient") WebClient settingsWebClient,
            AuthorizationTokenService authorizationTokenService) {
        this.settingsWebClient = settingsWebClient;
        this.authorizationTokenService = authorizationTokenService;
    }

    public AbpResponseBean updateAllSettings(UpdateAllSettingsRequestBean request) {
        String refreshToken = authorizationTokenService.getRefreshToken();
        String authorization = "Bearer " + refreshToken;
        
        return settingsWebClient.post()
                .uri("/webapigw/api/services/app/TenantSettings/UpdateAllSettings")
                .header("Authorization", authorization)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AbpResponseBean.class)
                .block();
    }

    public AbpResponseBean changeTenantSettings(Integer countryId, TenantAndCountrySettingsRequestBean request) {
        String refreshToken = authorizationTokenService.getRefreshToken();
        String authorization = "Bearer " + refreshToken;
        
        return settingsWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/webapigw/api/services/app/GeoSettings/ChangeTenantSettings")
                        .queryParam("countryId", countryId)
                        .build())
                .header("Authorization", authorization)
                .bodyValue(request.settings())
                .retrieve()
                .bodyToMono(AbpResponseBean.class)
                .block();
    }

    public AbpResponseBean updateCountrySettings(Integer countryId, TenantAndCountrySettingsRequestBean request) {
        String refreshToken = authorizationTokenService.getRefreshToken();
        String authorization = "Bearer " + refreshToken;
        
        return settingsWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/webapigw/api/services/app/GeoSettings/UpdateCountrySettings")
                        .queryParam("countryId", countryId)
                        .build())
                .header("Authorization", authorization)
                .bodyValue(request.settings())
                .retrieve()
                .bodyToMono(AbpResponseBean.class)
                .block();
    }

    public AbpResponseBean changeBranchSettings(Integer countryId, Integer branchId, TenantAndCountrySettingsRequestBean request) {
        String refreshToken = authorizationTokenService.getRefreshToken();
        String authorization = "Bearer " + refreshToken;
        
        return settingsWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/webapigw/api/services/app/GeoSettings/ChangeBranchSettings")
                        .queryParam("countryId", countryId)
                        .queryParam("branchId", branchId)
                        .build())
                .header("Authorization", authorization)
                .bodyValue(request.settings())
                .retrieve()
                .bodyToMono(AbpResponseBean.class)
                .block();
    }

    /**
     * Get operational countries.
     * This method automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @return The response containing all operational countries.
     */
    public GetOperationalCountriesResponseBean getOperationalCountries() {
        String refreshToken = authorizationTokenService.getRefreshToken();
        String authorization = "Bearer " + refreshToken;
        
        return settingsWebClient.get()
                .uri("/webapigw/api/services/app/Country/GetOperationalCountries")
                .header("Authorization", authorization)
                .retrieve()
                .bodyToMono(GetOperationalCountriesResponseBean.class)
                .block();
    }
}

