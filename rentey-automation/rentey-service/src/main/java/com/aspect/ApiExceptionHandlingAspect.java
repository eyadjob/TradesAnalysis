package com.aspect;

import com.annotation.HandleApiException;
import com.util.ApiExceptionHandler;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.lang.reflect.Method;

/**
 * Aspect to handle API exceptions for methods annotated with @HandleApiException.
 * Automatically catches and handles WebClientResponseException and generic exceptions
 * using the ApiExceptionHandler utility.
 */
@Component
@Aspect
public class ApiExceptionHandlingAspect {

    private static final Logger logger = LoggerFactory.getLogger(ApiExceptionHandlingAspect.class);
    private final ApiExceptionHandler apiExceptionHandler;

    public ApiExceptionHandlingAspect(ApiExceptionHandler apiExceptionHandler) {
        this.apiExceptionHandler = apiExceptionHandler;
    }

    @Around("@annotation(com.annotation.HandleApiException)")
    public Object handleApiException(ProceedingJoinPoint joinPoint) throws Throwable {
        // Get the annotation to extract the operation name
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        HandleApiException annotation = method.getAnnotation(HandleApiException.class);
        
        // Get operation name from annotation, or derive from method name if not provided
        String operation = annotation.operation();
        if (operation == null || operation.trim().isEmpty()) {
            operation = deriveOperationName(method.getName());
        }

        try {
            // Execute the method
            return joinPoint.proceed();
        } catch (WebClientResponseException e) {
            // Handle WebClientResponseException
            apiExceptionHandler.handleWebClientResponseException(e, operation);
            return null; // Unreachable, but required for compilation
        } catch (Exception e) {
            // Handle generic exceptions
            apiExceptionHandler.handleGenericException(e, operation);
            return null; // Unreachable, but required for compilation
        }
    }

    /**
     * Derives an operation name from the method name.
     * Converts camelCase method names to readable operation descriptions.
     * 
     * @param methodName The method name
     * @return A readable operation description
     */
    private String deriveOperationName(String methodName) {
        if (methodName == null || methodName.isEmpty()) {
            return "execute operation";
        }
        
        // Convert camelCase to space-separated words
        String operation = methodName.replaceAll("([a-z])([A-Z])", "$1 $2").toLowerCase();
        return operation;
    }
}

