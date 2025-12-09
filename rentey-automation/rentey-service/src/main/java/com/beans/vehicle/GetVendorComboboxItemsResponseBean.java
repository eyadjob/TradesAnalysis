package com.beans.vehicle;

import com.beans.interfaces.ResponsePayload;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Response bean for GetVendorComboboxItems API.
 */
public record GetVendorComboboxItemsResponseBean(
        @JsonProperty("result") VendorComboboxItemsResult result,
        @JsonProperty("targetUrl") String targetUrl,
        @JsonProperty("success") Boolean success,
        @JsonProperty("error") Object error,
        @JsonProperty("unAuthorizedRequest") Boolean unAuthorizedRequest,
        @JsonProperty("__abp") Boolean abp
) implements ResponsePayload {
    public record VendorComboboxItemsResult(
            @JsonProperty("items") List<VendorComboboxItem> items
    ) {
    }

    public record VendorComboboxItem(
            @JsonProperty("countryId") Integer countryId,
            @JsonProperty("value") String value,
            @JsonProperty("displayText") String displayText,
            @JsonProperty("isSelected") Boolean isSelected
    ) {
    }
}

