package com.beans.booking;

import com.beans.interfaces.RequestPayload;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Request bean for CalculateBillingInformation API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CalculateBillingInformationRequestBean(
        @JsonProperty("extras") List<Object> extras,
        @JsonProperty("userDiscounts") List<Object> userDiscounts,
        @JsonProperty("modelId") String modelId,
        @JsonProperty("year") Integer year,
        @JsonProperty("vehicleId") Integer vehicleId,
        @JsonProperty("rentalRateId") Integer rentalRateId,
        @JsonProperty("dropoffDate") String dropoffDate,
        @JsonProperty("pickupDate") String pickupDate,
        @JsonProperty("statusId") Integer statusId,
        @JsonProperty("voucherOperationTypeId") Integer voucherOperationTypeId,
        @JsonProperty("pickupBranchId") Integer pickupBranchId,
        @JsonProperty("dropoffBranchId") Integer dropoffBranchId,
        @JsonProperty("customerId") Integer customerId,
        @JsonProperty("categoryId") String categoryId,
        @JsonProperty("couponCode") String couponCode
) implements RequestPayload {
}

