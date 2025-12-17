package com.beans.booking;

import com.beans.interfaces.ResponsePayload;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Response bean for GetBranchAvailableModelsForBookingComboboxItems API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GetBranchAvailableModelsForBookingComboboxItemsResponseBean(
        @JsonProperty("result") BranchAvailableModelsResult result,
        @JsonProperty("targetUrl") String targetUrl,
        @JsonProperty("success") Boolean success,
        @JsonProperty("error") String error,
        @JsonProperty("unAuthorizedRequest") Boolean unAuthorizedRequest,
        @JsonProperty("__abp") Boolean abp
) implements ResponsePayload {
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record BranchAvailableModelsResult(
            @JsonProperty("items") List<CategoryItem> items
    ) {
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CategoryItem(
            @JsonProperty("bookingAvailableModelComboboxItems") List<ModelItem> bookingAvailableModelComboboxItems,
            @JsonProperty("value") String value,
            @JsonProperty("displayText") String displayText,
            @JsonProperty("isSelected") Boolean isSelected
    ) {
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ModelItem(
            @JsonProperty("yearsComboboxItems") List<YearItem> yearsComboboxItems,
            @JsonProperty("value") String value,
            @JsonProperty("displayText") String displayText,
            @JsonProperty("isSelected") Boolean isSelected
    ) {
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record YearItem(
            @JsonProperty("value") String value,
            @JsonProperty("displayText") String displayText,
            @JsonProperty("isSelected") Boolean isSelected
    ) {
    }
}
