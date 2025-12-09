package com.beans.general;

import com.beans.interfaces.ResponsePayload;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response bean for UploadBase64File API.
 */
public record UploadBase64FileResponseBean(
        @JsonProperty("result") FileUploadResult result,
        @JsonProperty("targetUrl") String targetUrl,
        @JsonProperty("success") Boolean success,
        @JsonProperty("error") String error,
        @JsonProperty("unAuthorizedRequest") Boolean unAuthorizedRequest,
        @JsonProperty("__abp") Boolean abp
) implements ResponsePayload {
    public record FileUploadResult(
            @JsonProperty("fileId") String fileId,
            @JsonProperty("fileName") String fileName,
            @JsonProperty("virtualPath") String virtualPath,
            @JsonProperty("size") Double size
    ) {
    }
}

