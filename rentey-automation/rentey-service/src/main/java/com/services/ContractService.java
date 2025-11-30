package com.services;

import com.beans.GetCountriesPhoneResponseBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ContractService {

    private final WebClient settingsWebClient;
    private final AuthorizationTokenService authorizationTokenService;

    public ContractService(
            @Qualifier("settingsWebClient") WebClient settingsWebClient,
            AuthorizationTokenService authorizationTokenService) {
        this.settingsWebClient = settingsWebClient;
        this.authorizationTokenService = authorizationTokenService;
    }

    /**
     * Get countries phone information.
     * This method automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param typeId The type ID for filtering countries (required).
     * @param includeInActive Whether to include inactive countries (default: false).
     * @param includeNotAssign Whether to include not assigned countries (default: false).
     * @return The response containing all countries phone information.
     */
    public GetCountriesPhoneResponseBean getCountriesPhone(
            Integer typeId,
            Boolean includeInActive,
            Boolean includeNotAssign) {
        String refreshToken = authorizationTokenService.getRefreshToken();
        String authorization = "Bearer " + refreshToken;
        
        return settingsWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/webapigw/api/services/app/Country/GetCountriesPhone")
                        .queryParam("typeId", typeId)
                        .queryParam("includeInActive", includeInActive)
                        .queryParam("includeNotAssign", includeNotAssign)
                        .build())
                .header("Authorization", authorization)
                .retrieve()
                .bodyToMono(GetCountriesPhoneResponseBean.class)
                .block();
    }
}

