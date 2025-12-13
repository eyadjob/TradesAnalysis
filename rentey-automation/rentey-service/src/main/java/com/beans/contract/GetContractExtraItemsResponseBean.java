package com.beans.contract;

import com.beans.interfaces.ResponsePayload;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Response bean for GetContractExtraItems API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GetContractExtraItemsResponseBean(
        @JsonProperty("result") List<ContractExtraItem> result,
        @JsonProperty("targetUrl") String targetUrl,
        @JsonProperty("success") Boolean success,
        @JsonProperty("error") Object error,
        @JsonProperty("unAuthorizedRequest") Boolean unAuthorizedRequest,
        @JsonProperty("__abp") Boolean abp
) implements ResponsePayload {
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ContractExtraItem(
            @JsonProperty("extraId") Integer extraId,
            @JsonProperty("isMandatorySelection") Boolean isMandatorySelection,
            @JsonProperty("price") Price price,
            @JsonProperty("quantity") Integer quantity,
            @JsonProperty("maxQuantity") Integer maxQuantity,
            @JsonProperty("notIntersectWith") List<String> notIntersectWith,
            @JsonProperty("isSelected") Boolean isSelected,
            @JsonProperty("extraConfigurationId") Integer extraConfigurationId,
            @JsonProperty("extraTypeId") Integer extraTypeId,
            @JsonProperty("iconUrl") String iconUrl,
            @JsonProperty("name") String name,
            @JsonProperty("code") String code,
            @JsonProperty("shortDescription") String shortDescription,
            @JsonProperty("longDescription") String longDescription,
            @JsonProperty("termsAndConditions") String termsAndConditions
    ) {
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Price(
            @JsonProperty("value") Double value,
            @JsonProperty("max") Double max,
            @JsonProperty("description") String description,
            @JsonProperty("isoCode") String isoCode,
            @JsonProperty("currencyId") Integer currencyId
    ) {
    }
}

