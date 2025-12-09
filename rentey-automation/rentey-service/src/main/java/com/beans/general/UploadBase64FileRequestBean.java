package com.beans.general;

import com.beans.interfaces.RequestPayload;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request bean for UploadBase64File API.
 * The payload is a base64 encoded file string (e.g., "data:image/jpeg;base64,...").
 */
public record UploadBase64FileRequestBean(
        @JsonProperty("data") String data
) implements RequestPayload {
}

