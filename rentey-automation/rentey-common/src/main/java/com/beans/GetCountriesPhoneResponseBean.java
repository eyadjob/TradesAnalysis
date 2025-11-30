package com.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Response bean for GetCountriesPhone API.
 */
public record GetCountriesPhoneResponseBean(
        @JsonProperty("result") CountriesPhoneResult result,
        @JsonProperty("targetUrl") String targetUrl,
        @JsonProperty("success") Boolean success,
        @JsonProperty("error") String error,
        @JsonProperty("unAuthorizedRequest") Boolean unAuthorizedRequest,
        @JsonProperty("__abp") Boolean abp
) {
    public record CountriesPhoneResult(
            @JsonProperty("items") List<CountryPhoneItem> items
    ) {
    }

    public record CountryPhoneItem(
            @JsonProperty("name") String name,
            @JsonProperty("phoneCode") String phoneCode,
            @JsonProperty("flagUrl") String flagUrl,
            @JsonProperty("flagId") Integer flagId,
            @JsonProperty("id") Integer id
    ) {
    }
}

