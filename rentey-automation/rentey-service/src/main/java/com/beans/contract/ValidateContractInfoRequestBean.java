package com.beans.contract;

import com.beans.interfaces.RequestPayload;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request bean for ValidateContractInfo API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ValidateContractInfoRequestBean(
        @JsonProperty("vehicleId") Integer vehicleId,
        @JsonProperty("pickupBranchId") Integer pickupBranchId,
        @JsonProperty("dropoffBranchId") Integer dropoffBranchId,
        @JsonProperty("pickupDate") String pickupDate,
        @JsonProperty("dropoffDate") String dropoffDate,
        @JsonProperty("bookingId") String bookingId,
        @JsonProperty("customerInfo") CustomerInfo customerInfo,
        @JsonProperty("skipTajeerIntegration") Boolean skipTajeerIntegration
) implements RequestPayload {
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CustomerInfo(
            @JsonProperty("driverId") Long driverId,
            @JsonProperty("identityId") Long identityId
    ) {
    }
}

