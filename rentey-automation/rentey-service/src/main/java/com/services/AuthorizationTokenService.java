package com.services;

import com.beans.authentication.AuthenticateRequestBean;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private AuthorizationService authorizationService;

    @Value("${authorization.service.credentials.userNameOrEmailAddress}")
    private String userNameOrEmailAddress;

    @Value("${authorization.service.credentials.password}")
    private String password;

    @Value("${authorization.service.credentials.rememberClient}")
    private Boolean rememberClient;

    /**
     * Retrieves a refresh token by calling the internal AuthorizationService.
     * This method now uses the internal AuthorizationService instead of the external authorization-service module.
     * 
     * @return The refresh token string
     * @throws RuntimeException if the token cannot be retrieved or if there's a connection error
     */
    public String getRefreshToken() {
        // Create authentication request with only authentication fields
        // Configuration fields are not needed since we're calling the internal API directly
        // (they will be null and won't be serialized due to Jackson's non_null configuration)
        AuthenticateRequestBean authRequest = new AuthenticateRequestBean(
                userNameOrEmailAddress,
                password,
                rememberClient,
                null,  // twoFactorRememberClientToken
                false, // singleSignIn
                null,  // returnUrl
                // Configuration fields are null - not needed for internal API call
                null,  // baseUrl
                null,  // tenantId
                null,  // userAgent
                null,  // accept
                null,  // acceptLanguage
                null,  // acceptEncoding
                null,  // pragma
                null,  // cacheControl
                null,  // expires
                null,  // xRequestedWith
                null,  // aspnetcoreCulture
                null,  // origin
                null   // referer
        );

        org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(AuthorizationTokenService.class);
        logger.info("Requesting token from internal AuthorizationService");

        try {
            var authResponse = authorizationService.authenticate(authRequest);
            
            if (authResponse == null || authResponse.result() == null || (authResponse.result().refreshToken() == null && authResponse.result().accessToken() == null )) {
                throw new RuntimeException("Failed to get refresh token from AuthorizationService. Response: " + authResponse);
            }

            logger.info("Successfully retrieved refresh token from AuthorizationService");
            if (authResponse.result().refreshToken() != null && !authResponse.result().refreshToken().isEmpty()) {
                return authResponse.result().refreshToken();
            } else {
                return authResponse.result().accessToken();
            }

        } catch (Exception e) {
            logger.error("Error calling AuthorizationService: {}", e.getMessage(), e);
            throw new RuntimeException("Error calling AuthorizationService: " + e.getMessage(), e);
        }
    }
}

