package com.configs;

import com.filters.AuthorizationHeaderFilter;
import com.filters.WebClientLoggingFilter;
import com.services.AuthorizationTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.netty.http.client.HttpClient;
import io.netty.channel.ChannelOption;

@Configuration
public class RenteyConfiguration {

    @Bean("settingsWebClient")
    public WebClient renteyWebClient(
            AuthorizationTokenService authorizationTokenService,
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
                // Add Authorization header filter FIRST, so it's applied to all requests
                // This filter dynamically retrieves the token from authorization-service and adds it as "Bearer <token>"
                .filter(AuthorizationHeaderFilter.addAuthorizationHeader(authorizationTokenService))
                // Add logging filter AFTER authorization, so we can see the Authorization header in logs
                .filter(WebClientLoggingFilter.logRequestAndResponse())
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

    @Bean("apiBasePath")
    public String apiBasePath(@Value("${settings.api.api-base-path}") String apiBasePath) {
        return apiBasePath;
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
    public com.clients.AuthorizationServiceClient authorizationServiceClient(
            @Value("${authorization.service.base-url}") String baseUrl,
            @Value("${authorization.service.response-timeout-seconds}") int responseTimeoutSeconds,
            @Value("${authorization.service.connect-timeout-millis}") int connectTimeoutMillis) {
        // Configure WebClient with configurable timeouts for authorization service
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(java.time.Duration.ofSeconds(responseTimeoutSeconds))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeoutMillis);
        
        WebClient webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .defaultHeader(HttpHeaders.ACCEPT, "application/json")
                .build();
        
        return HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(webClient))
                .build()
                .createClient(com.clients.AuthorizationServiceClient.class);
    }

}

