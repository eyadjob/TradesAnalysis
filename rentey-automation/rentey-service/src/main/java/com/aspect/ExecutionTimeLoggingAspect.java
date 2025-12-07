package com.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Aspect to log execution time for methods annotated with @LogExecutionTime.
 */
@Component
@Aspect
public class ExecutionTimeLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(ExecutionTimeLoggingAspect.class);

    @Around(value = "@annotation(com.annotation.LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        Object result = null;
        Throwable exception = null;
        
        try {
            // Execute the method
            result = joinPoint.proceed();
        } catch (Throwable e) {
            // Capture the exception to log it, but don't re-throw yet
            exception = e;
        } finally {
            // Always calculate and log execution time, regardless of success or failure
            long executionTime = System.currentTimeMillis() - startTime;
            
            if (exception != null) {
                // Log execution time with error indication
                logger.error("Method {}.{} failed after {} ms with error: {} - Exception type: {}", 
                        className,
                        methodName,
                        executionTime,
                        exception.getMessage(),
                        exception.getClass().getSimpleName());
            } else {
                // Log execution time for successful execution
                logger.info("Method {}.{} executed successfully in {} ms", 
                        className,
                        methodName,
                        executionTime);
            }
        }
        
        // Re-throw the exception if one occurred
        if (exception != null) {
            throw exception;
        }
        
        return result;
    }
}

