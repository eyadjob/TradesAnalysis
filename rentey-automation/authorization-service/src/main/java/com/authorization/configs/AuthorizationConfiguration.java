package com.authorization.configs;

import com.authorization.clients.AuthorizationClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class AuthorizationConfiguration {

    @Bean
    public WebClient webClient(
            @Value("${authorization.api.base-url}") String baseUrl,
            @Value("${authorization.api.headers.tenant-id}") String tenantId,
            @Value("${authorization.api.headers.user-agent}") String userAgent,
            @Value("${authorization.api.headers.accept}") String accept,
            @Value("${authorization.api.headers.accept-language}") String acceptLanguage,
            @Value("${authorization.api.headers.accept-encoding}") String acceptEncoding,
            @Value("${authorization.api.headers.pragma}") String pragma,
            @Value("${authorization.api.headers.cache-control}") String cacheControl,
            @Value("${authorization.api.headers.expires}") String expires,
            @Value("${authorization.api.headers.x-requested-with}") String xRequestedWith,
            @Value("${authorization.api.headers.aspnetcore-culture}") String aspNetCoreCulture,
            @Value("${authorization.api.headers.origin}") String origin,
            @Value("${authorization.api.headers.referer}") String referer) {
        
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONNECTION, "keep-alive")
                .defaultHeader("Abp.TenantId", tenantId)
                .defaultHeader(HttpHeaders.USER_AGENT, userAgent)
                .defaultHeader(HttpHeaders.ACCEPT, accept)
                .defaultHeader(HttpHeaders.ACCEPT_LANGUAGE, acceptLanguage)
                .defaultHeader(HttpHeaders.ACCEPT_ENCODING, acceptEncoding)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .defaultHeader(HttpHeaders.PRAGMA, pragma)
                .defaultHeader(HttpHeaders.CACHE_CONTROL, cacheControl)
                .defaultHeader(HttpHeaders.EXPIRES, expires)
                .defaultHeader("X-Requested-With", xRequestedWith)
                .defaultHeader(".AspNetCore.Culture", aspNetCoreCulture)
                .defaultHeader(HttpHeaders.ORIGIN, origin)
                .defaultHeader(HttpHeaders.REFERER, referer)
                .build();
    }

    @Bean
    public AuthorizationClient authorizationClient(WebClient webClient) {
        return HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(webClient))
                .build()
                .createClient(AuthorizationClient.class);
    }
}

