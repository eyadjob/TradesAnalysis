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
        long startNanoTime = System.nanoTime();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        
        // Log that the aspect is being invoked (for debugging)
        logger.debug("LogExecutionTime aspect invoked for method {}.{}", className, methodName);
        
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
            long executionTimeNanos = System.nanoTime() - startNanoTime;
            
            // Format execution time for better readability
            String timeFormatted = formatExecutionTime(executionTime, executionTimeNanos);
            
            if (exception != null) {
                // Log execution time with error indication
                logger.error("[EXECUTION TIME] Method {}.{} failed after {} ({} ms) with error: {} - Exception type: {}", 
                        className,
                        methodName,
                        timeFormatted,
                        executionTime,
                        exception.getMessage(),
                        exception.getClass().getSimpleName());
            } else {
                // Log execution time for successful execution
                // Use INFO level to ensure it's logged with a clear prefix
                logger.info("[EXECUTION TIME] Method {}.{} executed successfully in {} ({} ms)", 
                        className,
                        methodName,
                        timeFormatted,
                        executionTime);
            }
        }
        
        // Re-throw the exception if one occurred
        if (exception != null) {
            throw exception;
        }
        
        return result;
    }
    
    /**
     * Formats execution time in a human-readable format.
     * 
     * @param millis Execution time in milliseconds
     * @param nanos Execution time in nanoseconds (for precision)
     * @return Formatted string representation
     */
    private String formatExecutionTime(long millis, long nanos) {
        if (millis < 1) {
            return String.format("%.2f ms", nanos / 1_000_000.0);
        } else if (millis < 1000) {
            return String.format("%d ms", millis);
        } else {
            long seconds = millis / 1000;
            long remainingMillis = millis % 1000;
            return String.format("%d s %d ms", seconds, remainingMillis);
        }
    }
}

