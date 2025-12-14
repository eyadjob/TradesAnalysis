package com.beans.booking;

import com.beans.interfaces.ResponsePayload;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response bean for GetBestRentalRateForModel API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GetBestRentalRateForModelResponseBean(
        @JsonProperty("result") RentalRate result,
        @JsonProperty("targetUrl") String targetUrl,
        @JsonProperty("success") Boolean success,
        @JsonProperty("error") Object error,
        @JsonProperty("unAuthorizedRequest") Boolean unAuthorizedRequest,
        @JsonProperty("__abp") Boolean abp
) implements ResponsePayload {
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record RentalRate(
            @JsonProperty("rentalRateId") Integer rentalRateId,
            @JsonProperty("dailyRate") Double dailyRate,
            @JsonProperty("originalDailyRate") Double originalDailyRate,
            @JsonProperty("extraKMRate") Double extraKMRate,
            @JsonProperty("periodName") String periodName,
            @JsonProperty("periodId") Integer periodId,
            @JsonProperty("freeKM") Integer freeKM,
            @JsonProperty("currencyIso") String currencyIso,
            @JsonProperty("utilizationRateDiscountPercentage") Double utilizationRateDiscountPercentage,
            @JsonProperty("utilizationRateId") Integer utilizationRateId,
            @JsonProperty("hasOffers") Boolean hasOffers,
            @JsonProperty("hasUtilizationRateOrOffers") Boolean hasUtilizationRateOrOffers,
            @JsonProperty("currencyId") Integer currencyId
    ) {
    }
}

