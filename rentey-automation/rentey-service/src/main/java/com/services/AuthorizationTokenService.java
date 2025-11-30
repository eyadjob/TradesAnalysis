package com.services;

import com.beans.AuthenticateRequestBean;
import com.clients.AuthorizationServiceClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service responsible for managing authorization tokens.
 * 
 * Following SOLID principles:
 * - Single Responsibility: Handles only token retrieval and management
 * - DRY: Centralizes token logic to eliminate duplication
 * - Open/Closed: Can be extended without modifying existing code
 */
@Service
public class AuthorizationTokenService {

    private final AuthorizationServiceClient authorizationServiceClient;
    private final AuthorizationServiceManager authorizationServiceManager;
    private final String userNameOrEmailAddress;
    private final String password;
    private final Boolean rememberClient;

    public AuthorizationTokenService(
            AuthorizationServiceClient authorizationServiceClient,
            AuthorizationServiceManager authorizationServiceManager,
            @Value("${authorization.service.credentials.userNameOrEmailAddress}") String userNameOrEmailAddress,
            @Value("${authorization.service.credentials.password}") String password,
            @Value("${authorization.service.credentials.rememberClient}") Boolean rememberClient) {
        this.authorizationServiceClient = authorizationServiceClient;
        this.authorizationServiceManager = authorizationServiceManager;
        this.userNameOrEmailAddress = userNameOrEmailAddress;
        this.password = password;
        this.rememberClient = rememberClient;
    }

    /**
     * Retrieves a refresh token from the authorization service.
     * This method automatically ensures authorization-service is running before making the call.
     * 
     * @return The refresh token string
     * @throws RuntimeException if the token cannot be retrieved or if there's a connection error
     */
    public String getRefreshToken() {
        try {
            // Ensure authorization-service is running before making the call
            authorizationServiceManager.ensureAuthorizationServiceIsRunning();
        } catch (RuntimeException e) {
            // If authorization-service failed to start, wrap the error with more context
            throw new RuntimeException(
                "Failed to start authorization-service automatically: " + e.getMessage() + 
                ". Please ensure authorization-service can be started or start it manually.",
                e
            );
        }
        
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
                    "The service may have failed to start automatically. " +
                    "Please check the logs or start it manually: cd authorization-service && mvnw spring-boot:run",
                    e
                );
            }
            throw new RuntimeException("Error calling authorization-service: " + e.getMessage(), e);
        }
    }
}

