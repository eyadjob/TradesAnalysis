package com.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration for WebClient to call rentey-service APIs.
 */
@Configuration
public class ImportCustomerConfiguration {

    @Bean("renteyServiceWebClient")
    public WebClient renteyServiceWebClient(@Value("${rentey.service.base-url}") String baseUrl) {
        // Increase buffer limit to 10MB to handle large responses
        final int size = 10 * 1024 * 1024;
        final ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
                .build();
        
        return WebClient.builder()
                .baseUrl(baseUrl)
                .exchangeStrategies(strategies)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .defaultHeader(HttpHeaders.ACCEPT, "application/json")
                .build();
    }
}

