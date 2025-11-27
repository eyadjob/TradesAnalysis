package com.controllers;

import com.beans.UpdateAllSettingsRequest;
import com.beans.UpdateAllSettingsResponse;
import com.services.SettingsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/services/app/TenantSettings")
public class SettingsController {

    private final SettingsService settingsService;

    public SettingsController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    /**
     * Update all settings for the tenant.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param request The request containing all the settings to be updated.
     * @return The response containing the result of the operation.
     */
    @PostMapping(path = "/UpdateAllSettings", consumes = "application/json", produces = "application/json")
    public UpdateAllSettingsResponse updateAllSettings(
            @RequestBody(required = true) UpdateAllSettingsRequest request) {

        if (request == null) {
            throw new IllegalArgumentException("Request body is required and cannot be null.");
        }

        return settingsService.updateAllSettings(request);
    }
}

