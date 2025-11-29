package com.configs;

import com.clients.AuthorizationServiceClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RenteyConfiguration {

    @Bean("settingsWebClient")
    public WebClient settingsWebClient(
            @Value("${settings.api.base-url}") String baseUrl,
            @Value("${settings.api.headers.tenant-id}") String tenantId,
            @Value("${settings.api.headers.user-agent}") String userAgent,
            @Value("${settings.api.headers.accept}") String accept,
            @Value("${settings.api.headers.accept-language}") String acceptLanguage,
            @Value("${settings.api.headers.accept-encoding}") String acceptEncoding,
            @Value("${settings.api.headers.pragma}") String pragma,
            @Value("${settings.api.headers.cache-control}") String cacheControl,
            @Value("${settings.api.headers.expires}") String expires,
            @Value("${settings.api.headers.x-requested-with}") String xRequestedWith,
            @Value("${settings.api.headers.aspnetcore-culture}") String aspNetCoreCulture,
            @Value("${settings.api.headers.origin}") String origin,
            @Value("${settings.api.headers.referer}") String referer) {
        
        // Increase buffer limit to 10MB to handle large responses
        final int size = 10 * 1024 * 1024;
        final ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
                .build();
        
        return WebClient.builder()
                .baseUrl(baseUrl)
                .exchangeStrategies(strategies)
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

    @Bean("authorizationWebClient")
    public WebClient authorizationWebClient(@Value("${authorization.service.base-url}") String baseUrl) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .defaultHeader(HttpHeaders.ACCEPT, "application/json")
                .build();
    }

    @Bean
    public AuthorizationServiceClient authorizationServiceClient(@Value("${authorization.service.base-url}") String baseUrl) {
        WebClient webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .defaultHeader(HttpHeaders.ACCEPT, "application/json")
                .build();
        
        return HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(webClient))
                .build()
                .createClient(AuthorizationServiceClient.class);
    }

}

