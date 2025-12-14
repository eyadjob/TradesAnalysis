package com.beans.contract;

import com.beans.interfaces.ResponsePayload;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response bean for GetOpenContractDateInputs API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GetOpenContractDateInputsResponseBean(
        @JsonProperty("result") OpenContractDateInputs result,
        @JsonProperty("targetUrl") String targetUrl,
        @JsonProperty("success") Boolean success,
        @JsonProperty("error") Object error,
        @JsonProperty("unAuthorizedRequest") Boolean unAuthorizedRequest,
        @JsonProperty("__abp") Boolean abp
) implements ResponsePayload {
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record OpenContractDateInputs(
            @JsonProperty("nowDate") String nowDate,
            @JsonProperty("dropoffDate") String dropoffDate
    ) {
    }
}

