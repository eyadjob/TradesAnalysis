package com.beans.setting;

import com.beans.interfaces.RequestPayload;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.Map;

public record TenantAndCountrySettingsRequestBean(
        Map<String, TenantSetting> settings
) implements RequestPayload {
    public record TenantSetting(
            @JsonProperty("id") Integer id,
            @JsonProperty("creationTime") LocalDateTime creationTime,
            @JsonProperty("creatorUserId") Integer creatorUserId,
            @JsonProperty("lastModifierUserId") Integer lastModifierUserId,
            @JsonProperty("name") String name,
            @JsonProperty("value") String value,
            @JsonProperty("countryId") Integer countryId,
            @JsonProperty("branchId") Integer branchId,
            @JsonProperty("isInherited") Boolean isInherited,
            @JsonProperty("tenantId") Integer tenantId
    ) {
    }
}

