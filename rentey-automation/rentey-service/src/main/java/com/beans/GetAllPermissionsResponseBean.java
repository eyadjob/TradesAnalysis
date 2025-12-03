package com.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

public record GetAllPermissionsResponseBean(
        @JsonProperty("result") PermissionResult result,
        @JsonProperty("targetUrl") String targetUrl,
        @JsonProperty("success") Boolean success,
        @JsonProperty("error") String error,
        @JsonProperty("unAuthorizedRequest") Boolean unAuthorizedRequest,
        @JsonProperty("__abp") Boolean abp
) implements ResponsePayload {
    public record PermissionResult(
            @JsonProperty("items") List<PermissionItem> items
    ) {
    }

    public record PermissionItem(
            @JsonProperty("level") Integer level,
            @JsonProperty("id") String id,
            @JsonProperty("parentName") String parentName,
            @JsonProperty("name") String name,
            @JsonProperty("displayName") String displayName,
            @JsonProperty("description") String description,
            @JsonProperty("permissionGroupId") String permissionGroupId,
            @JsonProperty("groupName") String groupName,
            @JsonProperty("isGrantedByDefault") Boolean isGrantedByDefault,
            @JsonProperty("multiTenancySides") Integer multiTenancySides,
            @JsonProperty("properties") Map<String, Object> properties
    ) {
    }
}

