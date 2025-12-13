package com.beans.lookups;

import com.beans.interfaces.ResponsePayload;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Response bean for GetItemsByType API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GetItemsByTypeResponseBean(
        @JsonProperty("result") ItemsByTypeResult result,
        @JsonProperty("targetUrl") String targetUrl,
        @JsonProperty("success") Boolean success,
        @JsonProperty("error") Object error,
        @JsonProperty("unAuthorizedRequest") Boolean unAuthorizedRequest,
        @JsonProperty("__abp") Boolean abp
) implements ResponsePayload {
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ItemsByTypeResult(
            @JsonProperty("items") List<LookupItem> items
    ) {
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record LookupItem(
            @JsonProperty("text") String text,
            @JsonProperty("parentId") Integer parentId,
            @JsonProperty("lookupTypeId") Integer lookupTypeId,
            @JsonProperty("sequence") Integer sequence,
            @JsonProperty("tenantId") Integer tenantId,
            @JsonProperty("isActive") Boolean isActive,
            @JsonProperty("value") String value,
            @JsonProperty("countryISOCode") String countryISOCode,
            @JsonProperty("countryId") Integer countryId,
            @JsonProperty("features") String features,
            @JsonProperty("guid") String guid,
            @JsonProperty("isAxIntegration") Boolean isAxIntegration,
            @JsonProperty("isDeleted") Boolean isDeleted,
            @JsonProperty("deleterUserId") Integer deleterUserId,
            @JsonProperty("deletionTime") String deletionTime,
            @JsonProperty("lastModificationTime") String lastModificationTime,
            @JsonProperty("lastModifierUserId") Integer lastModifierUserId,
            @JsonProperty("creationTime") String creationTime,
            @JsonProperty("creatorUserId") Integer creatorUserId,
            @JsonProperty("id") Integer id
    ) {
    }
}

