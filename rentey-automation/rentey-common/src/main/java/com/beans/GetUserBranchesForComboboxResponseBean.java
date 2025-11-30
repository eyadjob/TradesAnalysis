package com.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Response bean for GetUserBranchesForCombobox API.
 */
public record GetUserBranchesForComboboxResponseBean(
        @JsonProperty("result") BranchesResult result,
        @JsonProperty("targetUrl") String targetUrl,
        @JsonProperty("success") Boolean success,
        @JsonProperty("error") String error,
        @JsonProperty("unAuthorizedRequest") Boolean unAuthorizedRequest,
        @JsonProperty("__abp") Boolean abp
) {
    public record BranchesResult(
            @JsonProperty("items") List<BranchItem> items
    ) {
    }

    public record BranchItem(
            @JsonProperty("isActive") Boolean isActive,
            @JsonProperty("locations") List<LocationItem> locations,
            @JsonProperty("isAirport") Boolean isAirport,
            @JsonProperty("value") String value,
            @JsonProperty("displayText") String displayText,
            @JsonProperty("isSelected") Boolean isSelected
    ) {
    }

    public record LocationItem(
            @JsonProperty("value") String value,
            @JsonProperty("displayText") String displayText,
            @JsonProperty("isSelected") Boolean isSelected
    ) {
    }
}

