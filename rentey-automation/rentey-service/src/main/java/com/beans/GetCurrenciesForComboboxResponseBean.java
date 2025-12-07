package com.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Response bean for GetCurrenciesForCombobox API.
 */
public record GetCurrenciesForComboboxResponseBean(
        @JsonProperty("result") CurrenciesResult result,
        @JsonProperty("targetUrl") String targetUrl,
        @JsonProperty("success") Boolean success,
        @JsonProperty("error") Object error,
        @JsonProperty("unAuthorizedRequest") Boolean unAuthorizedRequest,
        @JsonProperty("__abp") Boolean abp
) implements ResponsePayload {
    public record CurrenciesResult(
            @JsonProperty("items") List<CurrencyItem> items
    ) {
    }

    public record CurrencyItem(
            @JsonProperty("isoCode") String isoCode,
            @JsonProperty("value") String value,
            @JsonProperty("displayText") String displayText,
            @JsonProperty("isSelected") Boolean isSelected
    ) {
    }
}

