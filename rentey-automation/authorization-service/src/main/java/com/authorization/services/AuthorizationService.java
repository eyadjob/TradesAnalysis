package com.authorization.services;

import com.authorization.annotation.LogRequestAndResponseOnDesk;
import com.authorization.beans.AuthenticateRequest;
import com.authorization.beans.AuthenticateResponse;
import com.authorization.clients.AuthorizationClient;
import com.authorization.configs.WebClientLoggingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Service
public class AuthorizationService {
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationService.class);

    @LogRequestAndResponseOnDesk
    public AuthenticateResponse authenticate(AuthenticateRequest request) {
        logger.info("authenticate service request received. baseUrl: {}, tenantId: {}", 
                request.baseUrl(), request.tenantId());
        
        try {
            // Create WebClient dynamically based on request parameters
            WebClient webClient = createWebClient(request);
            logger.debug("WebClient created with baseUrl: {}", request.baseUrl());
            
            // Create AuthorizationClient dynamically
            AuthorizationClient authorizationClient = HttpServiceProxyFactory
                    .builder(WebClientAdapter.forClient(webClient))
                    .build()
                    .createClient(AuthorizationClient.class);
            
            // Create a request without configuration fields for the external API call
            // Configuration fields are only used for WebClient setup above
            AuthenticateRequest apiRequest = request.toApiRequest();
            logger.info("Calling external API to authenticate. URL will be: {}/webapigw/api/TokenAuth/Authenticate", 
                    request.baseUrl());
            AuthenticateResponse response = authorizationClient.authenticate(apiRequest);
            
            if (response == null) {
                logger.error("Authentication response is null");
                throw new RuntimeException("Authentication response is null");
            }
            
            if (response.result() == null) {
                logger.error("Authentication response result is null. Error: {}", response.error());
                throw new RuntimeException("Authentication failed: " + response.error());
            }
            
            logger.info("Authentication successful. Token received: {}", 
                    response.result().refreshToken() != null ? "yes" : "no");
            return response;
        } catch (Exception e) {
            logger.error("Error during authentication: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to authenticate with external API: " + e.getMessage(), e);
        }
    }

    @LogRequestAndResponseOnDesk
    private WebClient createWebClient(AuthenticateRequest request) {
        // Use baseUrl from request (dynamically provided by requester)
        // Fallback to default only if requester didn't provide it (should not happen in normal operation)
        String baseUrl = request.baseUrl() != null && !request.baseUrl().isEmpty() 
                ? request.baseUrl() 
                : "http://172.86.86.151:2800"; // Fallback - should not be used if requester is properly configured
        
        if (request.baseUrl() == null || request.baseUrl().isEmpty()) {
            logger.warn("WARNING: baseUrl not provided in request, using fallback: {}", baseUrl);
        }
        
        logger.debug("Creating WebClient with baseUrl: {}", baseUrl);
        
        WebClient.Builder builder = WebClient.builder()
                .baseUrl(baseUrl)
                // Add logging filter to log all requests and responses
                .filter(WebClientLoggingFilter.logRequestAndResponse())
                .defaultHeader(HttpHeaders.CONNECTION, "keep-alive")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        
        // Add headers from request if provided, otherwise use defaults
        if (request.tenantId() != null && !request.tenantId().isEmpty()) {
            builder.defaultHeader("Abp.TenantId", request.tenantId());
        }
        if (request.userAgent() != null && !request.userAgent().isEmpty()) {
            builder.defaultHeader(HttpHeaders.USER_AGENT, request.userAgent());
        }
        if (request.accept() != null && !request.accept().isEmpty()) {
            builder.defaultHeader(HttpHeaders.ACCEPT, request.accept());
        }
        if (request.acceptLanguage() != null && !request.acceptLanguage().isEmpty()) {
            builder.defaultHeader(HttpHeaders.ACCEPT_LANGUAGE, request.acceptLanguage());
        }
        if (request.acceptEncoding() != null && !request.acceptEncoding().isEmpty()) {
            builder.defaultHeader(HttpHeaders.ACCEPT_ENCODING, request.acceptEncoding());
        }
        if (request.pragma() != null && !request.pragma().isEmpty()) {
            builder.defaultHeader(HttpHeaders.PRAGMA, request.pragma());
        }
        if (request.cacheControl() != null && !request.cacheControl().isEmpty()) {
            builder.defaultHeader(HttpHeaders.CACHE_CONTROL, request.cacheControl());
        }
        if (request.expires() != null && !request.expires().isEmpty()) {
            builder.defaultHeader(HttpHeaders.EXPIRES, request.expires());
        }
        if (request.xRequestedWith() != null && !request.xRequestedWith().isEmpty()) {
            builder.defaultHeader("X-Requested-With", request.xRequestedWith());
        }
        if (request.aspnetcoreCulture() != null && !request.aspnetcoreCulture().isEmpty()) {
            builder.defaultHeader(".AspNetCore.Culture", request.aspnetcoreCulture());
        }
        if (request.origin() != null && !request.origin().isEmpty()) {
            builder.defaultHeader(HttpHeaders.ORIGIN, request.origin());
        }
        if (request.referer() != null && !request.referer().isEmpty()) {
            builder.defaultHeader(HttpHeaders.REFERER, request.referer());
        }
        
        return builder.build();
    }
}

