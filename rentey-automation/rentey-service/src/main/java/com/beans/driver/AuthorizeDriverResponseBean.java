package com.beans.driver;

import com.beans.interfaces.ResponsePayload;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Response bean for AuthorizeDriver API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record AuthorizeDriverResponseBean(
        @JsonProperty("result") AuthorizeDriverResult result,
        @JsonProperty("targetUrl") String targetUrl,
        @JsonProperty("success") Boolean success,
        @JsonProperty("error") Object error,
        @JsonProperty("unAuthorizedRequest") Boolean unAuthorizedRequest,
        @JsonProperty("__abp") Boolean abp
) implements ResponsePayload {
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record AuthorizeDriverResult(
            @JsonProperty("correlationId") String correlationId,
            @JsonProperty("verificationCodeExpirationTimeInSeconds") Integer verificationCodeExpirationTimeInSeconds,
            @JsonProperty("validationOutput") ValidationOutput validationOutput
    ) {
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ValidationOutput(
            @JsonProperty("validations") List<Validation> validations,
            @JsonProperty("canContinue") Boolean canContinue,
            @JsonProperty("hasErrors") Boolean hasErrors
    ) {
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Validation(
            @JsonProperty("type") ValidationType type,
            @JsonProperty("message") String message,
            @JsonProperty("hasPermission") Boolean hasPermission,
            @JsonProperty("code") Integer code,
            @JsonProperty("errorCode") String errorCode
    ) {
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ValidationType(
            @JsonProperty("name") String name,
            @JsonProperty("id") Integer id
    ) {
    }
}

