package com.services;

import com.beans.AuthorizationAuthenticateRequest;
import com.beans.UpdateAllSettingsRequest;
import com.beans.UpdateAllSettingsResponse;
import com.clients.AuthorizationServiceClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class SettingsService {

    private final WebClient settingsWebClient;
    private final AuthorizationServiceClient authorizationServiceClient;
    private final String userNameOrEmailAddress;
    private final String password;
    private final Boolean rememberClient;

    public SettingsService(
            @Qualifier("settingsWebClient") WebClient settingsWebClient,
            AuthorizationServiceClient authorizationServiceClient,
            @Value("${authorization.service.credentials.userNameOrEmailAddress}") String userNameOrEmailAddress,
            @Value("${authorization.service.credentials.password}") String password,
            @Value("${authorization.service.credentials.rememberClient}") Boolean rememberClient) {
        this.settingsWebClient = settingsWebClient;
        this.authorizationServiceClient = authorizationServiceClient;
        this.userNameOrEmailAddress = userNameOrEmailAddress;
        this.password = password;
        this.rememberClient = rememberClient;
    }

    public UpdateAllSettingsResponse updateAllSettings(UpdateAllSettingsRequest request) {
        // First, call authorization-service to get the refreshToken
        AuthorizationAuthenticateRequest authRequest = new AuthorizationAuthenticateRequest(
                userNameOrEmailAddress,
                password,
                rememberClient,
                null,
                false,
                null
        );

        var authResponse = authorizationServiceClient.authenticate(authRequest);
        
        if (authResponse == null || authResponse.result() == null || authResponse.result().refreshToken() == null) {
            throw new RuntimeException("Failed to get refresh token from authorization-service. Response: " + authResponse);
        }

        String refreshToken = authResponse.result().refreshToken();
        String authorization = "Bearer " + refreshToken;
        
        // Then call the settings API with the refreshToken
        return settingsWebClient.post()
                .uri("/webapigw/api/services/app/TenantSettings/UpdateAllSettings")
                .header("Authorization", authorization)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(UpdateAllSettingsResponse.class)
                .block();
    }
}

