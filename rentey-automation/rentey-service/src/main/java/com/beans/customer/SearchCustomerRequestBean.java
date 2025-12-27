package com.beans.customer;

import com.beans.interfaces.RequestPayload;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request bean for SearchCustomer API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record SearchCustomerRequestBean(
        @JsonProperty("customerId") Long customerId,
        @JsonProperty("phoneNumber") String phoneNumber,
        @JsonProperty("identityNumber") String identityNumber
) implements RequestPayload {
}

