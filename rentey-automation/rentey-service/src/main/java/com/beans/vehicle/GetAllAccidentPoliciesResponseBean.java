package com.beans.vehicle;

import com.beans.interfaces.ResponsePayload;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * Response bean for GetAllAccidentPolicies API.
 */
public record GetAllAccidentPoliciesResponseBean(
        @JsonProperty("result") AccidentPoliciesResult result,
        @JsonProperty("targetUrl") String targetUrl,
        @JsonProperty("success") Boolean success,
        @JsonProperty("error") Object error,
        @JsonProperty("unAuthorizedRequest") Boolean unAuthorizedRequest,
        @JsonProperty("__abp") Boolean abp
) implements ResponsePayload {
    public record AccidentPoliciesResult(
            @JsonProperty("total") Integer total,
            @JsonProperty("data") List<AccidentPolicy> data
    ) {
    }

    public record AccidentPolicy(
            @JsonProperty("country") String country,
            @JsonProperty("updatedBy") String updatedBy,
            @JsonProperty("liabilityClause") String liabilityClause,
            @JsonProperty("endDate") OffsetDateTime endDate,
            @JsonProperty("policyNumber") String policyNumber,
            @JsonProperty("id") Integer id,
            @JsonProperty("isActive") Boolean isActive,
            @JsonProperty("isExpired") Boolean isExpired,
            @JsonProperty("insuranceCompany") String insuranceCompany,
            @JsonProperty("countryId") Integer countryId,
            @JsonProperty("startDate") OffsetDateTime startDate,
            @JsonProperty("lastUpdateTime") OffsetDateTime lastUpdateTime
    ) {
    }
}

