package com.beans.contract;

import com.beans.interfaces.ResponsePayload;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Response bean for GetExtrasNamesExcludedFromBookingPaymentDetails API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GetExtrasNamesExcludedFromBookingPaymentDetailsResponseBean(
        @JsonProperty("result") List<String> result,
        @JsonProperty("targetUrl") String targetUrl,
        @JsonProperty("success") Boolean success,
        @JsonProperty("error") Object error,
        @JsonProperty("unAuthorizedRequest") Boolean unAuthorizedRequest,
        @JsonProperty("__abp") Boolean abp
) implements ResponsePayload {
}

