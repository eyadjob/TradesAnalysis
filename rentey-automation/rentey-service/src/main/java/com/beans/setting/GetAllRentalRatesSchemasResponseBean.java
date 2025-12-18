package com.beans.setting;

import com.beans.interfaces.ResponsePayload;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Response bean for GetAllRentalRatesSchemas API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GetAllRentalRatesSchemasResponseBean(
        @JsonProperty("result") RentalRatesSchemasResult result,
        @JsonProperty("targetUrl") String targetUrl,
        @JsonProperty("success") Boolean success,
        @JsonProperty("error") String error,
        @JsonProperty("unAuthorizedRequest") Boolean unAuthorizedRequest,
        @JsonProperty("__abp") Boolean abp
) implements ResponsePayload {
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record RentalRatesSchemasResult(
            @JsonProperty("items") List<RentalRatesSchemaItem> items
    ) {
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record RentalRatesSchemaItem(
            @JsonProperty("name") String name,
            @JsonProperty("isActive") Boolean isActive,
            @JsonProperty("countryName") String countryName,
            @JsonProperty("creatorUserName") String creatorUserName,
            @JsonProperty("type") String type,
            @JsonProperty("creationTime") String creationTime,
            @JsonProperty("creatorUserId") Integer creatorUserId,
            @JsonProperty("id") Integer id
    ) {
    }
}
