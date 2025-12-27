package com.beans.contract;

import com.beans.interfaces.ResponsePayload;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Response bean for ValidateCustomer API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ValidateCustomerResponseBean(
        @JsonProperty("result") CustomerValidationResult result,
        @JsonProperty("targetUrl") String targetUrl,
        @JsonProperty("success") Boolean success,
        @JsonProperty("error") Object error,
        @JsonProperty("unAuthorizedRequest") Boolean unAuthorizedRequest,
        @JsonProperty("__abp") Boolean abp
) implements ResponsePayload {
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CustomerValidationResult(
            @JsonProperty("validations") List<Object> validations,
            @JsonProperty("canContinue") Boolean canContinue,
            @JsonProperty("hasErrors") Boolean hasErrors
    ) {
    }
}

