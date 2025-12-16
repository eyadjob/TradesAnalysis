package com.services;

import com.annotation.LogExecutionTime;
import com.annotation.LogRequestAndResponseOnDesk;
import com.beans.authentication.AuthenticateRequestBean;
import com.beans.authentication.AuthenticateResponseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Service for authentication-related API operations.
 * Handles user authentication and token management.
 */
@Service
public class AuthorizationService {

    @Autowired
    @Qualifier("authorizationServiceWebClient")
    private WebClient authorizationServiceWebClient;

    @Autowired
    @Qualifier("apiBasePathWithoutService")
    private String apiBasePathWithoutService;

    /**
     * Authenticate user and get access token.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     * This endpoint authenticates a user and returns access token, refresh token, and user information.
     * Results are cached based on the authentication request parameters.
     *
     * @param request The authentication request containing username/email, password, and other authentication parameters.
     * @return The response containing access token, refresh token, and user information.
     */
    @Cacheable(cacheNames = "authenticationCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    @LogRequestAndResponseOnDesk
    public AuthenticateResponseBean authenticate(AuthenticateRequestBean request) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        // Note: Configuration fields (baseUrl, tenantId, etc.) in AuthenticateRequestBean are ignored for this direct API call
        // They are only used when calling authorization-service
        return authorizationServiceWebClient.post()
                .uri(apiBasePathWithoutService + "/TokenAuth/Authenticate")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AuthenticateResponseBean.class)
                .block();
    }
}
