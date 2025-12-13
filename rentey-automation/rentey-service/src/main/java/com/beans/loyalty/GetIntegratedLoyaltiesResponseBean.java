package com.beans.loyalty;

import com.beans.interfaces.ResponsePayload;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Response bean for GetIntegratedLoyalties API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GetIntegratedLoyaltiesResponseBean(
        @JsonProperty("result") List<IntegratedLoyalty> result,
        @JsonProperty("targetUrl") String targetUrl,
        @JsonProperty("success") Boolean success,
        @JsonProperty("error") Object error,
        @JsonProperty("unAuthorizedRequest") Boolean unAuthorizedRequest,
        @JsonProperty("__abp") Boolean abp
) implements ResponsePayload {
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record IntegratedLoyalty(
            @JsonProperty("name") String name,
            @JsonProperty("validationExpression") String validationExpression,
            @JsonProperty("allowEarn") Boolean allowEarn,
            @JsonProperty("allowRedeem") Boolean allowRedeem,
            @JsonProperty("allowMandatoryVerification") Boolean allowMandatoryVerification,
            @JsonProperty("allowVerificationWhileAddingToTheCustomer") Boolean allowVerificationWhileAddingToTheCustomer,
            @JsonProperty("isBranchBased") Boolean isBranchBased,
            @JsonProperty("id") String id
    ) {
    }
}

