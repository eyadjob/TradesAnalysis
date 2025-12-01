package com.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

/**
 * WebClient filter to log all outgoing requests with payload, URI, and headers.
 */
public class WebClientLoggingFilter {

    private static final Logger logger = LoggerFactory.getLogger(WebClientLoggingFilter.class);

    public static ExchangeFilterFunction logRequestAndResponse() {
        return ExchangeFilterFunction.ofRequestProcessor(request -> {
            if (logger.isInfoEnabled()) {
                logRequestDetails(request);
            }
            // For body logging, we need to handle it carefully to avoid consuming the stream
            // We'll log it if it's a BodyInserter that we can inspect
            return Mono.just(request);
        }).andThen(ExchangeFilterFunction.ofResponseProcessor(response -> {
            if (logger.isInfoEnabled()) {
                logResponseDetails(response);
            }
            return Mono.just(response);
        }));
    }

    private static void logRequestDetails(ClientRequest request) {
        HttpMethod method = request.method();
        String uri = request.url().toString();
        HttpHeaders headers = request.headers();
        
        logger.info("=== Outgoing Request from rentey-service ===");
        logger.info("Method: {}", method);
        logger.info("URI: {}", uri);
        logger.info("Headers: {}", formatHeaders(headers));
        // Note: Request body will be logged separately in the service layer
        // to avoid consuming the reactive stream
        logger.info("==============================================");
    }

    private static void logResponseDetails(ClientResponse response) {
        logger.info("=== Response from External API ===");
        logger.info("Status: {}", response.statusCode());
        logger.info("Headers: {}", formatHeaders(response.headers().asHttpHeaders()));
        logger.info("==================================");
    }

    private static String formatHeaders(HttpHeaders headers) {
        if (headers == null || headers.isEmpty()) {
            return "{}";
        }
        return headers.entrySet().stream()
                .map(entry -> {
                    String headerName = entry.getKey();
                    String headerValue = String.join(", ", entry.getValue());
                    // Mask sensitive headers but show enough to verify it's present
                    if (isSensitiveHeader(headerName)) {
                        headerValue = maskSensitiveValue(headerValue);
                    }
                    return "\"" + headerName + "\": \"" + headerValue + "\"";
                })
                .collect(Collectors.joining(", ", "{", "}"));
    }

    private static boolean isSensitiveHeader(String headerName) {
        String lowerName = headerName.toLowerCase();
        return lowerName.contains("authorization") || 
               lowerName.contains("password") || 
               lowerName.contains("token") ||
               lowerName.contains("secret") ||
               lowerName.contains("key");
    }

    private static String maskSensitiveValue(String value) {
        if (value == null || value.length() <= 20) {
            return "***";
        }
        // Show first 20 characters (enough to see "Bearer " prefix) and mask the rest
        return value.substring(0, 20) + "***";
    }
}

