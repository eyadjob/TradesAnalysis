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
            // Get token reactively, using cached token if available and not expired
            return getTokenReactive(authorizationTokenService)
                    .map(refreshToken -> {
                        if (refreshToken == null || refreshToken.trim().isEmpty()) {
                            logger.error("Refresh token is null or empty. Cannot add Authorization header.");
                            throw new RuntimeException("Refresh token is null or empty. Cannot add Authorization header.");
                        }
                        
                        String authorization = "Bearer " + refreshToken;
                        
                        // Add the Authorization header to the request
                        ClientRequest modifiedRequest = ClientRequest.from(request)
                                .header(HttpHeaders.AUTHORIZATION, authorization)
                                .build();
                        
                        logger.info("Added Authorization header to request: {} (token length: {})", 
                                request.url(), refreshToken != null ? refreshToken.length() : 0);
                        logger.debug("Authorization header value: Bearer {}...", 
                                refreshToken != null && refreshToken.length() > 10 
                                        ? refreshToken
                                        : "refresh token value seem invalid and it was: " + refreshToken    );
                        
                        return modifiedRequest;
                    })
                    .onErrorMap(e -> {
                        logger.error("Failed to add Authorization header to request: {}", e.getMessage(), e);
                        return new RuntimeException("Failed to add Authorization header: " + e.getMessage(), e);
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
                    logger.debug("Fetching new token from authorization-service");
                    String token = authorizationTokenService.getRefreshToken();
                    // Cache the token
                    tokenCache.set(new CachedToken(token, Instant.now().plus(TOKEN_CACHE_DURATION)));
                    return token;
                })
                .subscribeOn(Schedulers.boundedElastic()) // Run blocking call on bounded elastic scheduler
                .timeout(Duration.ofSeconds(35)) // Increase timeout to 35 seconds to allow for authorization service response (30s) + overhead
                .doOnError(e -> {
                    logger.error("Error getting token: {}", e.getMessage(), e);
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

