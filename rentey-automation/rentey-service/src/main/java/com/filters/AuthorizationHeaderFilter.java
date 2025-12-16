package com.filters;

import com.services.AuthorizationTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;

/**
 * WebClient filter that automatically adds the Authorization header
 * to all requests made through settingsWebClient.
 * Includes token caching to avoid calling authorization-service on every request.
 */
public class AuthorizationHeaderFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationHeaderFilter.class);
    
    // Token cache with expiration (5 minutes)
    private static final Duration TOKEN_CACHE_DURATION = Duration.ofMinutes(5);
    private static final AtomicReference<CachedToken> tokenCache = new AtomicReference<>();

    /**
     * Creates an ExchangeFilterFunction that automatically adds the Authorization header
     * with the Bearer token from AuthorizationTokenService to all requests.
     *
     * @param authorizationTokenService The service to get the refresh token from
     * @return ExchangeFilterFunction that adds the Authorization header
     */
    public static ExchangeFilterFunction addAuthorizationHeader(AuthorizationTokenService authorizationTokenService) {
        return ExchangeFilterFunction.ofRequestProcessor(request -> {
            logger.info("AuthorizationHeaderFilter: Processing request to {}", request.url());
            // Get token reactively, using cached token if available and not expired
            return getTokenReactive(authorizationTokenService)
                    .doOnNext(token -> logger.debug("Token retrieved successfully, length: {}", 
                            token != null ? token.length() : 0))
                    .doOnError(error -> logger.error("CRITICAL: Token retrieval failed for request to {}. Error: {}", 
                            request.url(), error.getMessage(), error))
                    .map(refreshToken -> {
                        if (refreshToken == null || refreshToken.trim().isEmpty()) {
                            logger.error("CRITICAL: Refresh token is null or empty for request to {}. Cannot add Authorization header.", 
                                    request.url());
                            throw new RuntimeException("Refresh token is null or empty. Cannot add Authorization header.");
                        }
                        
                        String authorization = "Bearer " + refreshToken;
                        
                        // Add the Authorization header to the request
                        ClientRequest modifiedRequest = ClientRequest.from(request)
                                .header(HttpHeaders.AUTHORIZATION, authorization)
                                .build();
                        
                        logger.info("Successfully added Authorization header to request: {} (token length: {})", 
                                request.url(), refreshToken.length());
                        logger.debug("Authorization header value: Bearer {}...", 
                                refreshToken.length() > 10 
                                        ? refreshToken.substring(0, 10) + "..."
                                        : "token too short: " + refreshToken);
                        
                        return modifiedRequest;
                    })
                    .doOnError(error -> logger.error("CRITICAL: Error in AuthorizationHeaderFilter for request to {}. Error: {}", 
                            request.url(), error.getMessage(), error))
                    .onErrorMap(e -> {
                        logger.error("CRITICAL: Mapping error in AuthorizationHeaderFilter for request to {}. Original error: {}", 
                                request.url(), e.getMessage(), e);
                        RuntimeException mappedError = new RuntimeException(
                                "Failed to add Authorization header to request " + request.url() + ": " + e.getMessage(), e);
                        return mappedError;
                    });
        });
    }

    /**
     * Gets the token reactively, using cache if available and not expired.
     */
    private static Mono<String> getTokenReactive(AuthorizationTokenService authorizationTokenService) {
        // Check cache first
        CachedToken cached = tokenCache.get();
        if (cached != null && !cached.isExpired()) {
            logger.debug("Using cached token");
            return Mono.just(cached.token);
        }

        // Get new token from service (run on blocking scheduler since it's a blocking call)
        return Mono.fromCallable(() -> {
                    logger.info("Fetching new token from authorization-service...");
                    try {
                        String token = authorizationTokenService.getRefreshToken();
                        if (token == null || token.trim().isEmpty()) {
                            logger.error("CRITICAL: authorization-service returned null or empty token");
                            throw new RuntimeException("Token retrieved from authorization-service is null or empty");
                        }
                        logger.info("Successfully retrieved token from authorization-service (length: {})", token.length());
                        // Cache the token
                        tokenCache.set(new CachedToken(token, Instant.now().plus(TOKEN_CACHE_DURATION)));
                        return token;
                    } catch (Exception e) {
                        logger.error("CRITICAL: Exception while fetching token from authorization-service: {}", e.getMessage(), e);
                        throw e;
                    }
                })
                .subscribeOn(Schedulers.boundedElastic()) // Run blocking call on bounded elastic scheduler
                .timeout(Duration.ofMinutes(5)) // Timeout set to 5 minutes to allow for authorization service response
                .doOnError(e -> {
                    logger.error("CRITICAL: Error in getTokenReactive: {}", e.getMessage(), e);
                    // Clear cache on error
                    tokenCache.set(null);
                });
    }

    /**
     * Internal class to hold cached token with expiration time.
     */
    private static class CachedToken {
        final String token;
        final Instant expiresAt;

        CachedToken(String token, Instant expiresAt) {
            this.token = token;
            this.expiresAt = expiresAt;
        }

        boolean isExpired() {
            return Instant.now().isAfter(expiresAt);
        }
    }
}

