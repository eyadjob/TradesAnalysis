package com.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Response bean for GetAllItemsComboboxItems API.
 */
public record GetAllItemsComboboxItemsResponseBean(
        @JsonProperty("result") ComboboxItemsResult result,
        @JsonProperty("targetUrl") String targetUrl,
        @JsonProperty("success") Boolean success,
        @JsonProperty("error") Object error,
        @JsonProperty("unAuthorizedRequest") Boolean unAuthorizedRequest,
        @JsonProperty("__abp") Boolean abp
) {
    public record ComboboxItemsResult(
            @JsonProperty("items") List<ComboboxItem> items
    ) {
    }

    public record ComboboxItem(
            @JsonProperty("value") String value,
            @JsonProperty("displayText") String displayText,
            @JsonProperty("isSelected") Boolean isSelected
    ) {
    }
}

