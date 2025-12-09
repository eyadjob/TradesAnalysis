package com.beans.vehicle;

import com.beans.interfaces.ResponsePayload;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Response bean for GetAllCarModels API.
 */
public record GetAllCarModelsResponseBean(
        @JsonProperty("result") CarModelsResult result,
        @JsonProperty("targetUrl") String targetUrl,
        @JsonProperty("success") Boolean success,
        @JsonProperty("error") Object error,
        @JsonProperty("unAuthorizedRequest") Boolean unAuthorizedRequest,
        @JsonProperty("__abp") Boolean abp
) implements ResponsePayload {
    public record CarModelsResult(
            @JsonProperty("items") List<CarModel> items
    ) {
    }

    public record CarModel(
            @JsonProperty("name") String name,
            @JsonProperty("manufactureId") Integer manufactureId,
            @JsonProperty("categoryId") Integer categoryId,
            @JsonProperty("isActive") Boolean isActive,
            @JsonProperty("image") ImageInfo image,
            @JsonProperty("creatorUserName") String creatorUserName,
            @JsonProperty("manufactureName") String manufactureName,
            @JsonProperty("categoryName") String categoryName,
            @JsonProperty("acrissCode") String acrissCode,
            @JsonProperty("lastModificationTime") OffsetDateTime lastModificationTime,
            @JsonProperty("lastModifierUserId") Integer lastModifierUserId,
            @JsonProperty("creationTime") OffsetDateTime creationTime,
            @JsonProperty("creatorUserId") Integer creatorUserId,
            @JsonProperty("id") Integer id
    ) {
    }

    public record ImageInfo(
            @JsonProperty("id") UUID id,
            @JsonProperty("url") String url,
            @JsonProperty("isNewDocument") Boolean isNewDocument
    ) {
    }
}

