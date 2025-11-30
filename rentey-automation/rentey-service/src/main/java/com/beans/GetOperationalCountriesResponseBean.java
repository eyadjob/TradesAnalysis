package com.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Response bean for GetOperationalCountries API.
 */
public record GetOperationalCountriesResponseBean(
        @JsonProperty("result") List<OperationalCountry> result,
        @JsonProperty("targetUrl") String targetUrl,
        @JsonProperty("success") Boolean success,
        @JsonProperty("error") String error,
        @JsonProperty("unAuthorizedRequest") Boolean unAuthorizedRequest,
        @JsonProperty("__abp") Boolean abp
) {
    public record OperationalCountry(
            @JsonProperty("id") Integer id,
            @JsonProperty("name") String name,
            @JsonProperty("branchesCount") Integer branchesCount,
            @JsonProperty("allowedIntegrations") List<String> allowedIntegrations
    ) {
    }
}
