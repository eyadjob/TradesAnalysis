package com.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateAllSettingsResponse(
        @JsonProperty("result") Object result,
        @JsonProperty("targetUrl") String targetUrl,
        @JsonProperty("success") Boolean success,
        @JsonProperty("error") String error,
        @JsonProperty("unAuthorizedRequest") Boolean unAuthorizedRequest,
        @JsonProperty("__abp") Boolean abp
) {
}

