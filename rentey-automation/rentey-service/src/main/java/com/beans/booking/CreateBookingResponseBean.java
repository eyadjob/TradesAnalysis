package com.beans.booking;

import com.beans.interfaces.ResponsePayload;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response bean for CreateBooking API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateBookingResponseBean(
        @JsonProperty("result") BookingResult result,
        @JsonProperty("targetUrl") String targetUrl,
        @JsonProperty("success") Boolean success,
        @JsonProperty("error") Object error,
        @JsonProperty("unAuthorizedRequest") Boolean unAuthorizedRequest,
        @JsonProperty("__abp") Boolean abp

) implements ResponsePayload {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record BookingResult(
            @JsonProperty("bookingId") Integer bookingId,
            @JsonProperty("bookingNumber") String bookingNumber
    ) {
    }
}

