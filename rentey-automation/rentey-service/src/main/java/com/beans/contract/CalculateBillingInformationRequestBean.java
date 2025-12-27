package com.beans.contract;

import com.beans.interfaces.RequestPayload;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Request bean for CalculateBillingInformation API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CalculateBillingInformationRequestBean(
        @JsonProperty("vehicleId") Integer vehicleId,
        @JsonProperty("rentalRateId") Integer rentalRateId,
        @JsonProperty("pickupDate") String pickupDate,
        @JsonProperty("dropoffDate") String dropoffDate,
        @JsonProperty("statusId") Integer statusId,
        @JsonProperty("voucherOperationTypeId") Integer voucherOperationTypeId,
        @JsonProperty("pickupBranchId") Integer pickupBranchId,
        @JsonProperty("dropoffBranchId") Integer dropoffBranchId,
        @JsonProperty("fuelOutId") Integer fuelOutId,
        @JsonProperty("extras") List<Object> extras,
        @JsonProperty("userDiscounts") List<Object> userDiscounts,
        @JsonProperty("offers") List<Object> offers,
        @JsonProperty("categoryId") Integer categoryId,
        @JsonProperty("driverAuthorizationTypeId") Integer driverAuthorizationTypeId,
        @JsonProperty("customerId") Long customerId,
        @JsonProperty("modelId") Integer modelId,
        @JsonProperty("contractType") Integer contractType
) implements RequestPayload {
}

