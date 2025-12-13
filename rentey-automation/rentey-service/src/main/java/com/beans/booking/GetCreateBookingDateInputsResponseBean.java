package com.beans.booking;

import com.beans.interfaces.ResponsePayload;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response bean for GetCreateBookingDateInputs API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GetCreateBookingDateInputsResponseBean(
        @JsonProperty("result") CreateBookingDateInputsResult result,
        @JsonProperty("targetUrl") String targetUrl,
        @JsonProperty("success") Boolean success,
        @JsonProperty("error") Object error,
        @JsonProperty("unAuthorizedRequest") Boolean unAuthorizedRequest,
        @JsonProperty("__abp") Boolean abp
) implements ResponsePayload {
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CreateBookingDateInputsResult(
            @JsonProperty("nowDate") String nowDate,
            @JsonProperty("minimumPickupDate") String minimumPickupDate,
            @JsonProperty("minimumCorporatePickupDate") String minimumCorporatePickupDate,
            @JsonProperty("minimumBrokerPickupDate") String minimumBrokerPickupDate,
            @JsonProperty("maximumPickupDate") String maximumPickupDate,
            @JsonProperty("countryIso") String countryIso
    ) {
    }
}

