package com.controllers;

import com.beans.GetAllItemsComboboxItemsResponseBean;
import com.services.LookupsService;
import org.springframework.web.bind.annotation.*;

import static com.controllers.ApiPaths.*;

/**
 * Controller for lookup-related endpoints.
 */
@RestController
@RequestMapping(path = BASE_PATH)
public class LookupsController {

    private final LookupsService lookupsService;

    public LookupsController(LookupsService lookupsService) {
        this.lookupsService = lookupsService;
    }

    /**
     * Get all types for combobox items.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @return The response containing all types combobox items.
     */
    @GetMapping(path = LOOKUPS_GET_TYPES_COMBOBOX, produces = "application/json")
    public GetAllItemsComboboxItemsResponseBean getTypesComboboxItems() {
        return lookupsService.getTypesComboboxItems();
    }
}

