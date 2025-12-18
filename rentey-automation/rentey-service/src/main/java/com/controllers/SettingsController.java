package com.controllers;

import com.beans.general.AbpResponseBean;

import com.beans.setting.GetAllRentalRatesSchemasResponseBean;
import com.beans.setting.GetOperationalCountriesResponseBean;
import com.beans.setting.TenantAndCountrySettingsRequestBean;
import com.beans.setting.UpdateAllSettingsRequestBean;
import com.services.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.controllers.ApiPaths.*;

@RestController
@RequestMapping(path = BASE_PATH)
public class SettingsController {

    @Autowired
    private SettingsService settingsService;

    /**
     * Update all settings for the tenant.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param request The request containing all the settings to be updated.
     * @return The response containing the result of the operation.
     */
    @PostMapping(path = TENANT_SETTINGS_UPDATE_ALL, consumes = "application/json", produces = "application/json")
    public AbpResponseBean updateAllSettings(
            @RequestBody(required = true) UpdateAllSettingsRequestBean request) {

        if (request == null) {
            throw new IllegalArgumentException("Request body is required and cannot be null.");
        }

        return settingsService.updateAllSettings(request);
    }

    /**
     * Change tenant settings for a specific country.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param countryId The country ID for which to change the settings.
     * @param request The request containing the settings to be changed.
     * @return The response containing the result of the operation.
     */
    @PostMapping(path = GEO_SETTINGS_CHANGE_TENANT, consumes = "application/json", produces = "application/json")
    public AbpResponseBean changeTenantSettings(
            @RequestParam(required = true) Integer countryId,
            @RequestBody(required = true) TenantAndCountrySettingsRequestBean request) {

        if (request == null) {
            throw new IllegalArgumentException("Request body is required and cannot be null.");
        }

        if (countryId == null) {
            throw new IllegalArgumentException("countryId parameter is required.");
        }

        return settingsService.changeTenantSettings(countryId, request);
    }

    /**
     * Update country settings for a specific country.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param countryId The country ID for which to update the settings.
     * @param request The request containing the settings to be updated.
     * @return The response containing the result of the operation.
     */
    @PostMapping(path = GEO_SETTINGS_UPDATE_COUNTRY, consumes = "application/json", produces = "application/json")
    public AbpResponseBean updateCountrySettings(
            @RequestParam(required = true) Integer countryId,
            @RequestBody(required = true) TenantAndCountrySettingsRequestBean request) {

        if (request == null) {
            throw new IllegalArgumentException("Request body is required and cannot be null.");
        }

        if (countryId == null) {
            throw new IllegalArgumentException("countryId parameter is required.");
        }

        return settingsService.updateCountrySettings(countryId, request);
    }

    /**
     * Change branch settings for a specific country and branch.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param countryId The country ID for which to change the branch settings.
     * @param branchId The branch ID for which to change the settings.
     * @param request The request containing the settings to be changed.
     * @return The response containing the result of the operation.
     */
    @PostMapping(path = GEO_SETTINGS_CHANGE_BRANCH, consumes = "application/json", produces = "application/json")
    public AbpResponseBean changeBranchSettings(
            @RequestParam(required = true) Integer countryId,
            @RequestParam(required = true) Integer branchId,
            @RequestBody(required = true) TenantAndCountrySettingsRequestBean request) {

        if (request == null) {
            throw new IllegalArgumentException("Request body is required and cannot be null.");
        }

        if (countryId == null) {
            throw new IllegalArgumentException("countryId parameter is required.");
        }

        if (branchId == null) {
            throw new IllegalArgumentException("branchId parameter is required.");
        }

        return settingsService.changeBranchSettings(countryId, branchId, request);
    }

    /**
     * Get operational countries.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @return The response containing all operational countries.
     */
    @GetMapping(path = COUNTRY_GET_OPERATIONAL_COUNTRIES, produces = "application/json")
    public GetOperationalCountriesResponseBean getOperationalCountries() {
        return settingsService.getOperationalCountries();
    }
}

