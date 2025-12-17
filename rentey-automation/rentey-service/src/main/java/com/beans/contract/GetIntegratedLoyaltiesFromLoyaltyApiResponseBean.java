package com.beans.contract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Response bean for GetIntegratedLoyalties from Loyalty API.
 * This is a direct array response (not wrapped in a result object).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GetIntegratedLoyaltiesFromLoyaltyApiResponseBean(
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
