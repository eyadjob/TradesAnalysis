package com.beans.driver;

import com.beans.interfaces.ResponsePayload;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Response bean for CancelDriverAuthorizationIfCancellationRequired API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CancelDriverAuthorizationIfCancellationRequiredResponseBean(
        @JsonProperty("result") DriverAuthorizationValidationResult result,
        @JsonProperty("targetUrl") String targetUrl,
        @JsonProperty("success") Boolean success,
        @JsonProperty("error") Object error,
        @JsonProperty("unAuthorizedRequest") Boolean unAuthorizedRequest,
        @JsonProperty("__abp") Boolean abp
) implements ResponsePayload {
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record DriverAuthorizationValidationResult(
            @JsonProperty("validations") List<Object> validations,
            @JsonProperty("canContinue") Boolean canContinue,
            @JsonProperty("hasErrors") Boolean hasErrors
    ) {
    }
}

