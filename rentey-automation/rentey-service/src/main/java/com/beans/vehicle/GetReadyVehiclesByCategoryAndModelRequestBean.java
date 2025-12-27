package com.beans.vehicle;

import com.beans.interfaces.RequestPayload;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request bean for GetReadyVehiclesByCategoryAndModel API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GetReadyVehiclesByCategoryAndModelRequestBean(
        @JsonProperty("categoryId") Integer categoryId,
        @JsonProperty("modelId") Integer modelId,
        @JsonProperty("branchId") Integer branchId,
        @JsonProperty("isBooking") Boolean isBooking,
        @JsonProperty("isMonthlyContract") Boolean isMonthlyContract
) implements RequestPayload {
}

