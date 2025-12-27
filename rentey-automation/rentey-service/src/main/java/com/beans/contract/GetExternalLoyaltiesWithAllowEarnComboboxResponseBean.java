package com.beans.contract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Response bean for GetExternalLoyaltiesWithAllowEarnCombobox API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GetExternalLoyaltiesWithAllowEarnComboboxResponseBean(
        @JsonProperty("items") List<ComboboxItem> items
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ComboboxItem(
            @JsonProperty("value") String value,
            @JsonProperty("displayText") String displayText,
            @JsonProperty("isSelected") Boolean isSelected
    ) {
    }
}

