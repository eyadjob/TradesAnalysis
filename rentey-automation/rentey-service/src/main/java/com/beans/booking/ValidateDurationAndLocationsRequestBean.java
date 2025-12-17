package com.beans.booking;

import com.beans.interfaces.RequestPayload;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request bean for ValidateDurationAndLocations API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ValidateDurationAndLocationsRequestBean(
        @JsonProperty("branchCountryId") Integer branchCountryId,
        @JsonProperty("pickupBranchId") Integer pickupBranchId,
        @JsonProperty("dropoffBranchId") Integer dropoffBranchId,
        @JsonProperty("pickupDate") String pickupDate,
        @JsonProperty("dropOffDate") String dropOffDate,
        @JsonProperty("bookingType") Integer bookingType,
        @JsonProperty("validatePickUpDate") Boolean validatePickUpDate,
        @JsonProperty("rentalRateSchemaPeriodId") Integer rentalRateSchemaPeriodId,
        @JsonProperty("contractDuration") Integer contractDuration
) implements RequestPayload {
}
