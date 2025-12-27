package com.beans.setting;

import com.beans.interfaces.ResponsePayload;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response bean for GetTenantSettingBySettingKey API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GetTenantSettingBySettingKeyResponseBean(
        @JsonProperty("result") TenantSetting result,
        @JsonProperty("targetUrl") String targetUrl,
        @JsonProperty("success") Boolean success,
        @JsonProperty("error") Object error,
        @JsonProperty("unAuthorizedRequest") Boolean unAuthorizedRequest,
        @JsonProperty("__abp") Boolean abp
) implements ResponsePayload {
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record TenantSetting(
            @JsonProperty("name") String name,
            @JsonProperty("value") String value,
            @JsonProperty("countryId") Integer countryId,
            @JsonProperty("branchId") Integer branchId,
            @JsonProperty("isInherited") Boolean isInherited,
            @JsonProperty("tenantId") Integer tenantId,
            @JsonProperty("lastModificationTime") String lastModificationTime,
            @JsonProperty("lastModifierUserId") Long lastModifierUserId,
            @JsonProperty("creationTime") String creationTime,
            @JsonProperty("creatorUserId") Long creatorUserId,
            @JsonProperty("id") Integer id
    ) {
    }
}

