package com.services;

import com.beans.GetAllItemsComboboxItemsResponseBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class LookupsService {

    private final WebClient settingsWebClient;
    private final AuthorizationTokenService authorizationTokenService;

    public LookupsService(
            @Qualifier("settingsWebClient") WebClient settingsWebClient,
            AuthorizationTokenService authorizationTokenService) {
        this.settingsWebClient = settingsWebClient;
        this.authorizationTokenService = authorizationTokenService;
    }

    /**
     * Get all items for a combobox based on the lookup type.
     * This method automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param typeId The type ID for the lookup items.
     * @param includeInActive Whether to include inactive items.
     * @param includeNotAssign Whether to include not assigned items.
     * @return The response containing all combobox items.
     */
    public GetAllItemsComboboxItemsResponseBean getAllItemsComboboxItems(
            Integer typeId,
            Boolean includeInActive,
            Boolean includeNotAssign) {
        String refreshToken = authorizationTokenService.getRefreshToken();
        String authorization = "Bearer " + refreshToken;
        
        return settingsWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/webapigw/api/services/app/Lookups/GetAllItemsComboboxItems")
                        .queryParam("typeId", typeId)
                        .queryParam("includeInActive", includeInActive)
                        .queryParam("includeNotAssign", includeNotAssign)
                        .build())
                .header("Authorization", authorization)
                .retrieve()
                .bodyToMono(GetAllItemsComboboxItemsResponseBean.class)
                .block();
    }
}

