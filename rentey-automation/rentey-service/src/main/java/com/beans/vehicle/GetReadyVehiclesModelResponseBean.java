package com.beans.vehicle;

import com.beans.interfaces.ResponsePayload;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Response bean for GetReadyVehiclesModel API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GetReadyVehiclesModelResponseBean(
        @JsonProperty("result") List<ReadyVehicleModel> result,
        @JsonProperty("targetUrl") String targetUrl,
        @JsonProperty("success") Boolean success,
        @JsonProperty("error") Object error,
        @JsonProperty("unAuthorizedRequest") Boolean unAuthorizedRequest,
        @JsonProperty("__abp") Boolean abp
) implements ResponsePayload {
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ReadyVehicleModel(
            @JsonProperty("modelId") Integer modelId,
            @JsonProperty("modelName") String modelName,
            @JsonProperty("year") Integer year,
            @JsonProperty("vehiclesIds") List<Integer> vehiclesIds,
            @JsonProperty("categoryId") Integer categoryId,
            @JsonProperty("categoryName") String categoryName,
            @JsonProperty("imageUrl") String imageUrl,
            @JsonProperty("vehiclesCount") Integer vehiclesCount,
            @JsonProperty("priceRange") PriceRange priceRange
    ) {
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PriceRange(
            @JsonProperty("text") String text,
            @JsonProperty("hasUtilizationRateOrOffers") Boolean hasUtilizationRateOrOffers
    ) {
    }
}

