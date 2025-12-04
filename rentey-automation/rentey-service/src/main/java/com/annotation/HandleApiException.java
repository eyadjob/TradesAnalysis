package com.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to enable automatic API exception handling for methods.
 * When applied to a method, the ApiExceptionHandlingAspect will automatically
 * catch and handle WebClientResponseException and generic exceptions.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HandleApiException {
    /**
     * A description of the operation for error messages.
     * Example: "create/update customer", "fetch user data", etc.
     * 
     * @return The operation description
     */
    String operation() default "";
}

