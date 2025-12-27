package com.beans.driver;

import com.beans.interfaces.RequestPayload;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request bean for CancelDriverAuthorizationIfCancellationRequired API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CancelDriverAuthorizationIfCancellationRequiredRequestBean(
        @JsonProperty("branchId") String branchId,
        @JsonProperty("contractMode") Integer contractMode,
        @JsonProperty("correlationId") Integer correlationId,
        @JsonProperty("driverAuthorizationTypeId") Integer driverAuthorizationTypeId,
        @JsonProperty("referenceId") String referenceId,
        @JsonProperty("referenceNumber") String referenceNumber,
        @JsonProperty("driverId") String driverId,
        @JsonProperty("end") String end,
        @JsonProperty("isManual") Boolean isManual,
        @JsonProperty("referenceTypeId") Integer referenceTypeId,
        @JsonProperty("start") String start,
        @JsonProperty("vehicleId") String vehicleId
) implements RequestPayload {
}

