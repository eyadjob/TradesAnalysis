package com.authorization.configs;

import com.authorization.filters.AuthorizationRequestLoggingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * Configuration class for authorization-service.
 * 
 * Note: WebClient and AuthorizationClient are now created dynamically
 * in AuthorizationService based on request parameters from the requester.
 * Static configuration has been removed to support dynamic baseUrl and headers.
 */
@Configuration
public class AuthorizationConfiguration {
    
    /**
     * Register AuthorizationRequestLoggingFilter to log all incoming HTTP requests
     * to the authorization-service (from rentey-service).
     * This filter logs request/response details including headers and body.
     */
    @Bean
    public FilterRegistrationBean<AuthorizationRequestLoggingFilter> authorizationRequestLoggingFilter() {
        FilterRegistrationBean<AuthorizationRequestLoggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AuthorizationRequestLoggingFilter());
        registrationBean.addUrlPatterns("/*"); // Apply to all URLs
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE); // Execute first
        registrationBean.setName("authorizationRequestLoggingFilter");
        return registrationBean;
    }
}

