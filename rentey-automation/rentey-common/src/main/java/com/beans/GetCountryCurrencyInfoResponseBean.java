package com.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response bean for GetCountryCurrencyInfo API.
 */
public record GetCountryCurrencyInfoResponseBean(
        @JsonProperty("result") CurrencyInfo result,
        @JsonProperty("targetUrl") String targetUrl,
        @JsonProperty("success") Boolean success,
        @JsonProperty("error") String error,
        @JsonProperty("unAuthorizedRequest") Boolean unAuthorizedRequest,
        @JsonProperty("__abp") Boolean abp
) {
    public record CurrencyInfo(
            @JsonProperty("name") String name,
            @JsonProperty("isoCode") String isoCode,
            @JsonProperty("decimalPlaces") Integer decimalPlaces,
            @JsonProperty("id") Integer id
    ) {
    }
}

