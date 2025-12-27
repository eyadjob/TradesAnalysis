package com.beans.customer;

import com.beans.interfaces.ResponsePayload;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response bean for IsCustomerEligibleForCustomerProvidersIntegration API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record IsCustomerEligibleForCustomerProvidersIntegrationResponseBean(
        @JsonProperty("result") CustomerEligibilityResult result,
        @JsonProperty("targetUrl") String targetUrl,
        @JsonProperty("success") Boolean success,
        @JsonProperty("error") Object error,
        @JsonProperty("unAuthorizedRequest") Boolean unAuthorizedRequest,
        @JsonProperty("__abp") Boolean abp
) implements ResponsePayload {
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CustomerEligibilityResult(
            @JsonProperty("isEligible") Boolean isEligible,
            @JsonProperty("identityTypeId") Integer identityTypeId,
            @JsonProperty("identityNumber") String identityNumber
    ) {
    }
}

