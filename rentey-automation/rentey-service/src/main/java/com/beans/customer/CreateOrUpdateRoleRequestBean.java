package com.beans.customer;

import com.beans.interfaces.RequestPayload;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record CreateOrUpdateRoleRequestBean(
        @JsonProperty("role") RoleInfo role,
        @JsonProperty("grantedPermissionNames") List<String> grantedPermissionNames,
        @JsonProperty("notifications") List<NotificationInfo> notifications
) implements RequestPayload {
    public record RoleInfo(
            @JsonProperty("id") Integer id,
            @JsonProperty("displayName") String displayName,
            @JsonProperty("isDefault") Boolean isDefault
    ) {
    }

    public record NotificationInfo(
            @JsonProperty("name") String name,
            @JsonProperty("isSubscribed") Boolean isSubscribed
    ) {
    }
}

