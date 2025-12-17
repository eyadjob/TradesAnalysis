package com.beans.booking;

import com.beans.interfaces.ResponsePayload;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Response bean for ValidateDurationAndLocations API.
 */
public record ValidateDurationAndLocationsResponseBean(
        @JsonProperty("result") ValidationResult result,
        @JsonProperty("targetUrl") String targetUrl,
        @JsonProperty("success") Boolean success,
        @JsonProperty("error") String error,
        @JsonProperty("unAuthorizedRequest") Boolean unAuthorizedRequest,
        @JsonProperty("__abp") Boolean abp
) implements ResponsePayload {
    
    public record ValidationResult(
            @JsonProperty("validations") List<Object> validations,
            @JsonProperty("canContinue") Boolean canContinue,
            @JsonProperty("hasErrors") Boolean hasErrors
    ) {
    }
}
