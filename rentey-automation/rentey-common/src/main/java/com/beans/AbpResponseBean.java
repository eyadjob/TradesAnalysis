package com.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Generic response bean for ABP framework API responses.
 * This bean follows the standard ABP response structure used across all endpoints.
 * 
 * Following SOLID principles:
 * - Single Responsibility: Represents a single concept (ABP API response)
 * - Open/Closed: Can be extended without modifying existing code
 * - DRY: Eliminates duplication across multiple response beans
 */
public record AbpResponseBean(
        @JsonProperty("result") Object result,
        @JsonProperty("targetUrl") String targetUrl,
        @JsonProperty("success") Boolean success,
        @JsonProperty("error") String error,
        @JsonProperty("unAuthorizedRequest") Boolean unAuthorizedRequest,
        @JsonProperty("__abp") Boolean abp
) {
}

