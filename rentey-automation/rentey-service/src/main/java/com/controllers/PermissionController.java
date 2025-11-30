package com.controllers;

import com.beans.AbpResponseBean;
import com.beans.CreateOrUpdateRoleRequestBean;
import com.beans.GetAllPermissionsResponseBean;
import com.services.PermissionService;
import org.springframework.web.bind.annotation.*;

import static com.controllers.ApiPaths.*;

@RestController
@RequestMapping(path = BASE_PATH)
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    /**
     * Get all permissions.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @return The response containing all permissions.
     */
    @GetMapping(path = PERMISSION_GET_ALL, produces = "application/json")
    public GetAllPermissionsResponseBean getAllPermissions() {
        return permissionService.getAllPermissions();
    }

    /**
     * Create or update a role.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param request The request containing role information, granted permissions, and notifications.
     * @return The response containing the result of the operation.
     */
    @PostMapping(path = ROLE_CREATE_OR_UPDATE, consumes = "application/json", produces = "application/json")
    public AbpResponseBean createOrUpdateRole(
            @RequestBody(required = true) CreateOrUpdateRoleRequestBean request) {

        if (request == null) {
            throw new IllegalArgumentException("Request body is required and cannot be null.");
        }

        return permissionService.createOrUpdateRole(request);
    }
}

