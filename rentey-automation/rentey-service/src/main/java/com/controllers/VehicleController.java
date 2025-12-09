package com.controllers;

import com.beans.general.GetAllItemsComboboxItemsResponseBean;
import com.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.controllers.ApiPaths.*;

/**
 * REST controller for vehicle-related operations.
 */
@RestController
@RequestMapping(path = BASE_PATH)
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    /**
     * Get insurance company combobox items.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param includeInActive Whether to include inactive insurance companies (default: false).
     * @param countryId The country ID for which to get the insurance companies (required).
     * @return The response containing all insurance company combobox items.
     */
    @GetMapping(path = INSURANCE_COMPANY_GET_COMBOBOX_ITEMS, produces = "application/json")
    public GetAllItemsComboboxItemsResponseBean getInsuranceCompanyComboboxItems(
            @RequestParam(required = false, defaultValue = "false") Boolean includeInActive,
            @RequestParam(required = true) Integer countryId) {

        if (countryId == null) {
            throw new IllegalArgumentException("countryId parameter is required.");
        }

        return vehicleService.getInsuranceCompanyComboboxItems(includeInActive, countryId);
    }
}
