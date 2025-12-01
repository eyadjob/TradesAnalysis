package com.filters;

import com.services.AuthorizationTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

/**
 * WebClient filter that automatically adds the Authorization header
 * to all requests made through settingsWebClient.
 */
public class AuthorizationHeaderFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationHeaderFilter.class);

    /**
     * Creates an ExchangeFilterFunction that automatically adds the Authorization header
     * with the Bearer token from AuthorizationTokenService to all requests.
     *
     * @param authorizationTokenService The service to get the refresh token from
     * @return ExchangeFilterFunction that adds the Authorization header
     */
    public static ExchangeFilterFunction addAuthorizationHeader(AuthorizationTokenService authorizationTokenService) {
        return ExchangeFilterFunction.ofRequestProcessor(request -> {
            try {
                // Get the refresh token from the authorization service
                String refreshToken = authorizationTokenService.getRefreshToken();
                
                if (refreshToken == null || refreshToken.trim().isEmpty()) {
                    logger.error("Refresh token is null or empty. Cannot add Authorization header.");
                    return Mono.error(new RuntimeException("Refresh token is null or empty. Cannot add Authorization header."));
                }
                
                String authorization = "Bearer " + refreshToken;
                
                // Add the Authorization header to the request
                ClientRequest modifiedRequest = ClientRequest.from(request)
                        .header(HttpHeaders.AUTHORIZATION, authorization)
                        .build();
                
                logger.debug("Added Authorization header to request: {}", request.url());
                
                return Mono.just(modifiedRequest);
            } catch (Exception e) {
                logger.error("Failed to add Authorization header to request: {}", e.getMessage(), e);
                return Mono.error(new RuntimeException("Failed to add Authorization header: " + e.getMessage(), e));
            }
        });
    }
}

