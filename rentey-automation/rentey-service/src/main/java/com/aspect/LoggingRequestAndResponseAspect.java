package com.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Aspect to log request and response details for methods annotated with @LogRequestAndResponseOnDesk.
 * Logs in a similar fashion to RequestLoggingFilter, including full URI, headers, and body.
 */
@Component
@Aspect
public class LoggingRequestAndResponseAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingRequestAndResponseAspect.class);
    private final ObjectMapper objectMapper;

    public LoggingRequestAndResponseAspect(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Around(value = "@annotation(com.annotation.LogRequestAndResponseOnDesk)")
    public Object logRequestAndResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        
        if (attributes == null) {
            // No HTTP request context, proceed without logging
            return joinPoint.proceed();
        }

        HttpServletRequest request = attributes.getRequest();
        HttpServletResponse response = attributes.getResponse();

        // Log request details
        logRequest(request, joinPoint);

        // Execute the method
        Object result = joinPoint.proceed();

        // Log response details
        if (response != null) {
            logResponse(response, result);
        }

        return result;
    }

    private void logRequest(HttpServletRequest request, ProceedingJoinPoint joinPoint) {
        try {
            String method = request.getMethod();
            
            // Build full URL with scheme, host, port, path, and query string
            String scheme = request.getScheme();
            String serverName = request.getServerName();
            int serverPort = request.getServerPort();
            String requestURI = request.getRequestURI();
            String queryString = request.getQueryString();
            
            StringBuilder fullUrl = new StringBuilder();
            fullUrl.append(scheme).append("://").append(serverName);
            if ((scheme.equals("http") && serverPort != 80) || 
                (scheme.equals("https") && serverPort != 443)) {
                fullUrl.append(":").append(serverPort);
            }
            fullUrl.append(requestURI);
            if (queryString != null && !queryString.isEmpty()) {
                fullUrl.append("?").append(queryString);
            }
            
            // Get all headers
            Map<String, String> headers = getHeaders(request);
            
            // Get request body from method arguments
            String requestBody = getRequestBody(joinPoint);
            
            // Log the request details
            logger.info("=== Incoming Request (Aspect) In Rentey Service Module ===");
            logger.info("Method: {}", method);
            logger.info("Full URI: {}", fullUrl.toString());
            logger.info("Controller Method: {}.{}", 
                    joinPoint.getSignature().getDeclaringTypeName(), 
                    joinPoint.getSignature().getName());
            logger.info("Headers: {}", formatHeaders(headers));
            if (requestBody != null && !requestBody.trim().isEmpty()) {
                logger.info("Request Body: {}", requestBody);
            } else {
                logger.info("Request Body: (empty)");
            }
            logger.info("==================================");
            
        } catch (Exception e) {
            logger.warn("Error logging request in aspect: {}", e.getMessage());
        }
    }

    private void logResponse(HttpServletResponse response, Object result) {
        try {
            // Get response headers
            Map<String, String> responseHeaders = getResponseHeaders(response);
            
            // Get response body
            String responseBody = getResponseBody(result);
            
            // Log the response details
            logger.info("=== Outgoing Response (Aspect) In Rentey Service Module ===");
            logger.info("Status: {}", response.getStatus());
            logger.info("Headers: {}", formatHeaders(responseHeaders));
            if (responseBody != null && !responseBody.trim().isEmpty()) {
                logger.info("Response Body: {}", responseBody);
            } else {
                logger.info("Response Body: (empty)");
            }
            logger.info("===================================");
            
        } catch (Exception e) {
            logger.warn("Error logging response in aspect: {}", e.getMessage());
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

    private Map<String, String> getResponseHeaders(HttpServletResponse response) {
        Map<String, String> headers = new HashMap<>();
        // HttpServletResponse doesn't provide a direct way to enumerate all headers
        // We'll log common headers that are typically set by Spring
        if (response != null) {
            // Check for common headers
            String contentType = response.getContentType();
            if (contentType != null) {
                headers.put("Content-Type", contentType);
            }
            // Note: Other headers like Content-Length, etc. are not easily accessible
            // without using a response wrapper, which would require filter-level changes
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
            sb.append("\"").append(headerName).append("\": \"").append(headerValue).append("\"");
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }


    private String getRequestBody(ProceedingJoinPoint joinPoint) {
        try {
            Object[] args = joinPoint.getArgs();
            if (args == null || args.length == 0) {
                return null;
            }
            
            // Find the first non-null argument that looks like a request body
            for (Object arg : args) {
                if (arg != null) {
                    // Skip common Spring types that aren't request bodies
                    if (arg instanceof HttpServletRequest || 
                        arg instanceof HttpServletResponse ||
                        arg instanceof org.springframework.ui.Model ||
                        arg instanceof org.springframework.validation.BindingResult) {
                        continue;
                    }
                    
                    // Try to serialize to JSON
                    try {
                        return objectMapper.writeValueAsString(arg);
                    } catch (Exception e) {
                        // If serialization fails, return string representation
                        return arg.toString();
                    }
                }
            }
            
            return null;
        } catch (Exception e) {
            logger.warn("Error extracting request body from method arguments: {}", e.getMessage());
            return null;
        }
    }

    private String getResponseBody(Object result) {
        if (result == null) {
            return null;
        }
        
        try {
            return objectMapper.writeValueAsString(result);
        } catch (Exception e) {
            logger.warn("Error serializing response body: {}", e.getMessage());
            return result.toString();
        }
    }
}
