package com.beans.setting;

import com.beans.interfaces.ResponsePayload;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * Response bean for GetBranchSettings API.
 */
public record GetBranchSettingsResponseBean(
        @JsonProperty("result") Map<String, String> result,
        @JsonProperty("targetUrl") String targetUrl,
        @JsonProperty("success") Boolean success,
        @JsonProperty("error") String error,
        @JsonProperty("unAuthorizedRequest") Boolean unAuthorizedRequest,
        @JsonProperty("__abp") Boolean abp
) implements ResponsePayload {
}
