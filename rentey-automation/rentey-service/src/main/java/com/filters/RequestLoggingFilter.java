package com.filters;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Filter to log all incoming requests with payload, URI, and headers.
 */
@Component
@Order(1)
public class RequestLoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    public void doFilter(jakarta.servlet.ServletRequest request, jakarta.servlet.ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Wrap request to cache the body content
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(httpRequest);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(httpResponse);

        try {
            chain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            // Log request details after processing
            logRequest(wrappedRequest);
            // Copy response body back to original response
            wrappedResponse.copyBodyToResponse();
        }
    }

    private void logRequest(ContentCachingRequestWrapper request) {
        try {
            // Extract request details
            String method = request.getMethod();
            String uri = request.getRequestURI();
            String queryString = request.getQueryString();
            String fullUri = queryString != null ? uri + "?" + queryString : uri;
            
            // Get all headers
            Map<String, String> headers = getHeaders(request);
            
            // Get request body
            String requestBody = getRequestBody(request);
            
            // Log the request details
            logger.info("=== Incoming Request ===");
            logger.info("Method: {}", method);
            logger.info("URI: {}", fullUri);
            logger.info("Headers: {}", formatHeaders(headers));
            if (requestBody != null && !requestBody.trim().isEmpty()) {
                logger.info("Request Body: {}", requestBody);
            } else {
                logger.info("Request Body: (empty)");
            }
            logger.info("========================");
            
        } catch (Exception e) {
            logger.warn("Error logging request: {}", e.getMessage());
        }
    }

    private Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                String headerValue = request.getHeader(headerName);
                headers.put(headerName, headerValue);
            }
        }
        return headers;
    }

    private String formatHeaders(Map<String, String> headers) {
        if (headers.isEmpty()) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        boolean first = true;
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            if (!first) {
                sb.append(", ");
            }
            // Mask sensitive headers
            String headerName = entry.getKey();
            String headerValue = entry.getValue();
            if (isSensitiveHeader(headerName)) {
                headerValue = maskSensitiveValue(headerValue);
            }
            sb.append("\"").append(headerName).append("\": \"").append(headerValue).append("\"");
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }

    private boolean isSensitiveHeader(String headerName) {
        String lowerName = headerName.toLowerCase();
        return lowerName.contains("authorization") || 
               lowerName.contains("password") || 
               lowerName.contains("token") ||
               lowerName.contains("secret") ||
               lowerName.contains("key");
    }

    private String maskSensitiveValue(String value) {
        if (value == null || value.length() <= 10) {
            return "***";
        }
        return value.substring(0, 10) + "***";
    }

    private String getRequestBody(ContentCachingRequestWrapper request) {
        byte[] content = request.getContentAsByteArray();
        if (content.length == 0) {
            return null;
        }
        
        String charset = request.getCharacterEncoding();
        if (charset == null || charset.trim().isEmpty()) {
            charset = "UTF-8";
        }
        
        try {
            return new String(content, java.nio.charset.Charset.forName(charset));
        } catch (java.nio.charset.IllegalCharsetNameException | java.nio.charset.UnsupportedCharsetException e) {
            logger.warn("Error decoding request body with charset {}: {}", charset, e.getMessage());
            return new String(content, java.nio.charset.StandardCharsets.UTF_8);
        }
    }
}

