package com.beans.driver;

import com.beans.interfaces.RequestPayload;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request bean for AuthorizeDriver API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record AuthorizeDriverRequestBean(
        @JsonProperty("branchId") String branchId,
        @JsonProperty("contractMode") Integer contractMode,
        @JsonProperty("correlationId") Integer correlationId,
        @JsonProperty("driverAuthorizationTypeId") Integer driverAuthorizationTypeId,
        @JsonProperty("driverId") String driverId,
        @JsonProperty("end") String end,
        @JsonProperty("isManual") Boolean isManual,
        @JsonProperty("phoneNumber") String phoneNumber,
        @JsonProperty("referenceTypeId") Integer referenceTypeId,
        @JsonProperty("start") String start,
        @JsonProperty("vehicleId") String vehicleId,
        @JsonProperty("verificationCode") Integer verificationCode
) implements RequestPayload {
}

