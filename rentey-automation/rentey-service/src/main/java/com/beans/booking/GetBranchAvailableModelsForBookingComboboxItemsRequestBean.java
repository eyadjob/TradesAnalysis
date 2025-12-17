package com.beans.booking;

import com.beans.interfaces.RequestPayload;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request bean for GetBranchAvailableModelsForBookingComboboxItems API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GetBranchAvailableModelsForBookingComboboxItemsRequestBean(
        @JsonProperty("branchId") Integer branchId,
        @JsonProperty("pickupDate") String pickupDate,
        @JsonProperty("dropOffDate") String dropOffDate,
        @JsonProperty("source") String source,
        @JsonProperty("bookingCategoryId") Integer bookingCategoryId,
        @JsonProperty("bookingModelId") Integer bookingModelId,
        @JsonProperty("bookingVehicleYear") Integer bookingVehicleYear
) implements RequestPayload {
}
