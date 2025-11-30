package com.services;

import com.beans.AbpResponseBean;
import com.beans.AuthenticateRequestBean;
import com.beans.CreateOrUpdateRoleRequestBean;
import com.beans.GetAllPermissionsResponseBean;
import com.clients.AuthorizationServiceClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class PermissionService {

    private final WebClient settingsWebClient;
    private final AuthorizationServiceClient authorizationServiceClient;
    private final String userNameOrEmailAddress;
    private final String password;
    private final Boolean rememberClient;

    public PermissionService(
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

    public GetAllPermissionsResponseBean getAllPermissions() {
        String refreshToken = getRefreshToken();
        String authorization = "Bearer " + refreshToken;
        
        return settingsWebClient.get()
                .uri("/webapigw/api/services/app/Permission/GetAllPermissions")
                .header("Authorization", authorization)
                .retrieve()
                .bodyToMono(GetAllPermissionsResponseBean.class)
                .block();
    }

    public AbpResponseBean createOrUpdateRole(CreateOrUpdateRoleRequestBean request) {
        String refreshToken = getRefreshToken();
        String authorization = "Bearer " + refreshToken;
        
        return settingsWebClient.post()
                .uri("/webapigw/api/services/app/Role/CreateOrUpdateRole")
                .header("Authorization", authorization)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AbpResponseBean.class)
                .block();
    }

    private String getRefreshToken() {
        AuthenticateRequestBean authRequest = new AuthenticateRequestBean(
                userNameOrEmailAddress,
                password,
                rememberClient,
                null,
                false,
                null
        );

        try {
            var authResponse = authorizationServiceClient.authenticate(authRequest);
            
            if (authResponse == null || authResponse.result() == null || authResponse.result().refreshToken() == null) {
                throw new RuntimeException("Failed to get refresh token from authorization-service. Response: " + authResponse);
            }

            return authResponse.result().refreshToken();
        } catch (org.springframework.web.reactive.function.client.WebClientException e) {
            if (e.getCause() instanceof java.net.ConnectException) {
                throw new RuntimeException(
                    "Cannot connect to authorization-service at http://localhost:8088. " +
                    "Please ensure the authorization-service is running. " +
                    "Start it by running: cd authorization-service && mvnw spring-boot:run",
                    e
                );
            }
            throw new RuntimeException("Error calling authorization-service: " + e.getMessage(), e);
        }
    }
}

