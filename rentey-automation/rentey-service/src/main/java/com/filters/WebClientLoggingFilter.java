package com.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.http.HttpCookie;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserter;
import java.net.URI;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * WebClient filter to log all outgoing requests with payload, URI, and headers.
 */
public class WebClientLoggingFilter {

    private static final Logger logger = LoggerFactory.getLogger(WebClientLoggingFilter.class);

    public static ExchangeFilterFunction logRequestAndResponse() {
        return ExchangeFilterFunction.ofRequestProcessor(request -> {
            if (logger.isInfoEnabled()) {
                // Log request details including body
                return logRequestDetailsWithBody(request);
            }
            return Mono.just(request);
        }).andThen(ExchangeFilterFunction.ofResponseProcessor(response -> {
            if (logger.isInfoEnabled()) {
                // Log response details including body
                return logResponseDetailsWithBody(response);
            }
            return Mono.just(response);
        }));
    }

    /**
     * Logs request details including the body.
     * Uses ExchangeFilterFunction to intercept the body during the exchange.
     */
    private static Mono<ClientRequest> logRequestDetailsWithBody(ClientRequest request) {
        HttpMethod method = request.method();
        String uri = request.url().toString();
        HttpHeaders headers = request.headers();

        logger.info("=== Outgoing Request from rentey-service ===");
        logger.info("Method: {}", method);
        logger.info("URI: {}", uri);
        logger.info("Headers: {}", formatHeaders(headers));

        // Only try to log body for methods that typically have bodies
        if (method == HttpMethod.POST || method == HttpMethod.PUT || method == HttpMethod.PATCH) {
            // Wrap the body inserter to intercept and log the body
            @SuppressWarnings("unchecked")
            BodyInserter<?, ClientHttpRequest> originalBody = (BodyInserter<?, ClientHttpRequest>) request.body();
            BodyInserter<Object, ClientHttpRequest> loggingBody = (outputMessage, context) -> {
                // Create a wrapper that captures the body as it's written
                LoggingClientHttpRequest loggingRequest = new LoggingClientHttpRequest(outputMessage);
                return originalBody.insert(loggingRequest, context);
            };

            ClientRequest newRequest = ClientRequest.from(request)
                    .body((BodyInserter<?, ClientHttpRequest>) loggingBody)
                    .build();

            return Mono.just(newRequest);
        } else {
            // For GET, DELETE, etc., no body to log
            logger.info("Request Body: (no body for {} method)", method);
            logger.info("==============================================");
            return Mono.just(request);
        }
    }

    /**
     * Wrapper for ClientHttpRequest that logs the body as it's being written.
     */
    private static class LoggingClientHttpRequest implements ClientHttpRequest {
        private final ClientHttpRequest delegate;
        private final StringBuilder bodyBuilder = new StringBuilder();
        private boolean bodyLogged = false;

        public LoggingClientHttpRequest(ClientHttpRequest delegate) {
            this.delegate = delegate;
        }

        @Override
        public org.springframework.http.HttpMethod getMethod() {
            return delegate.getMethod();
        }

        @Override
        public URI getURI() {
            return delegate.getURI();
        }

        @Override
        public MultiValueMap<String, HttpCookie> getCookies() {
            return delegate.getCookies();
        }

        @Override
        public <T> T getNativeRequest() {
            return delegate.getNativeRequest();
        }

        @Override
        public DataBufferFactory bufferFactory() {
            return delegate.bufferFactory();
        }

        @Override
        public void beforeCommit(java.util.function.Supplier<? extends Mono<Void>> action) {
            delegate.beforeCommit(action);
        }

        @Override
        public boolean isCommitted() {
            return delegate.isCommitted();
        }

        @Override
        public Mono<Void> writeWith(org.reactivestreams.Publisher<? extends DataBuffer> body) {
            // Capture and log the body
            return Flux.from(body)
                    .collectList()
                    .flatMap(buffers -> {
                        // Combine all buffers into a single string
                        for (DataBuffer buffer : buffers) {
                            byte[] bytes = new byte[buffer.readableByteCount()];
                            int position = buffer.readPosition();
                            buffer.read(bytes);
                            buffer.readPosition(position); // Reset position for reuse
                            bodyBuilder.append(new String(bytes, StandardCharsets.UTF_8));
                        }
                        
                        // Log the body once
                        if (!bodyLogged) {
                            String bodyString = bodyBuilder.toString();
                            if (bodyString != null && !bodyString.trim().isEmpty()) {
                                logger.info("Request Body: {}", bodyString);
                            } else {
                                logger.info("Request Body: (empty)");
                            }
                            logger.info("==============================================");
                            bodyLogged = true;
                        }
                        
                        // Write the original buffers to the delegate
                        return delegate.writeWith(Flux.fromIterable(buffers));
                    });
        }

        @Override
        public Mono<Void> writeAndFlushWith(org.reactivestreams.Publisher<? extends org.reactivestreams.Publisher<? extends DataBuffer>> body) {
            return delegate.writeAndFlushWith(body);
        }

        @Override
        public Mono<Void> setComplete() {
            return delegate.setComplete();
        }

        @Override
        public HttpHeaders getHeaders() {
            return delegate.getHeaders();
        }
    }

    /**
     * Logs response details including the body.
     * For large responses, skips body logging to avoid buffer limit issues.
     * Properly caches the body so it can be read multiple times.
     */
    private static Mono<ClientResponse> logResponseDetailsWithBody(ClientResponse response) {
        logger.info("=== Response from External API ===");
        logger.info("Status: {}", response.statusCode());
        logger.info("Headers: {}", formatHeaders(response.headers().asHttpHeaders()));
        
        // Check Content-Length header to decide if we should attempt body logging
        String contentLength = response.headers().asHttpHeaders().getFirst(HttpHeaders.CONTENT_LENGTH);
        boolean isLargeResponse = false;
        if (contentLength != null) {
            try {
                long length = Long.parseLong(contentLength);
                // Skip body logging for responses larger than 500KB to avoid buffer limit issues
                isLargeResponse = length > 500 * 1024;
            } catch (NumberFormatException e) {
                // If we can't parse content length, assume it might be large
            }
        }
        
        if (isLargeResponse) {
            // For large responses, skip body logging to avoid buffer limit exceptions
            logger.info("Response Body: (skipped - response too large for logging, size: {} bytes)", 
                    contentLength != null ? contentLength : "unknown");
            logger.info("==================================");
            // Return response as-is - the service will handle deserialization with proper codec config
            return Mono.just(response);
        }
        
        // For smaller responses, buffer the body properly so it can be read multiple times
        // We need to read the body as DataBuffers, copy them, log it, then recreate the response
        DataBufferFactory bufferFactory = new DefaultDataBufferFactory();
        
        return response.bodyToFlux(DataBuffer.class)
                .collectList()
                .flatMap(dataBuffers -> {
                    // Copy DataBuffers and build body string for logging
                    StringBuilder bodyBuilder = new StringBuilder();
                    java.util.List<DataBuffer> copiedBuffers = new java.util.ArrayList<>(dataBuffers.size());
                    
                    for (DataBuffer buffer : dataBuffers) {
                        // Copy the buffer so we can reuse it
                        byte[] bytes = new byte[buffer.readableByteCount()];
                        int position = buffer.readPosition();
                        buffer.read(bytes);
                        buffer.readPosition(position); // Reset position for original buffer
                        
                        // Create a copy of the buffer for the cached response
                        DataBuffer copiedBuffer = bufferFactory.wrap(bytes);
                        copiedBuffers.add(copiedBuffer);
                        
                        // Build string for logging
                        bodyBuilder.append(new String(bytes, StandardCharsets.UTF_8));
                    }
                    
                    String bodyString = bodyBuilder.toString();
                    
                    // Log the response body
                    if (bodyString != null && !bodyString.trim().isEmpty()) {
                        if (bodyString.length() > 10000) {
                            logger.info("Response Body (truncated, size: {} bytes): {}...", 
                                    bodyString.length(), bodyString.substring(0, 10000));
                        } else {
                            logger.info("Response Body: {}", bodyString);
                        }
                    } else {
                        logger.info("Response Body: (empty)");
                    }
                    logger.info("==================================");
                    
                    // Create a new cached response with the copied DataBuffers
                    // This allows the body to be read multiple times
                    ClientResponse cachedResponse = ClientResponse.from(response)
                            .body(Flux.fromIterable(copiedBuffers))
                            .build();
                    
                    return Mono.just(cachedResponse);
                })
                .timeout(java.time.Duration.ofSeconds(30))
                .onErrorResume(error -> {
                    // If body reading fails (buffer limit or other), skip body logging
                    if (error instanceof org.springframework.core.io.buffer.DataBufferLimitException) {
                        logger.info("Response Body: (too large to log - buffer limit exceeded)");
                    } else {
                        logger.warn("Could not read response body for logging: {}", error.getMessage());
                        logger.info("Response Body: (skipped)");
                    }
                    logger.info("==================================");
                    // Return original response - let the service handle it
                    return Mono.just(response);
                });
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

