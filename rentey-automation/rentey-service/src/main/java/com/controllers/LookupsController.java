package com.controllers;


import com.services.LookupsService;
import com.beans.GetAllItemsComboboxItemsResponseBean;
import org.springframework.web.bind.annotation.*;

import static com.controllers.ApiPaths.*;

@RestController
@RequestMapping(path = BASE_PATH)
public class LookupsController {

    private final LookupsService lookupsService;

    public LookupsController(LookupsService lookupsService) {
        this.lookupsService = lookupsService;
    }

    /**
     * Get all items for a combobox based on the lookup type.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param typeId The type ID for the lookup items (required).
     * @param includeInActive Whether to include inactive items (default: false).
     * @param includeNotAssign Whether to include not assigned items (default: false).
     * @return The response containing all combobox items.
     */
    @GetMapping(path = LOOKUPS_GET_ALL_ITEMS_COMBOBOX, produces = "application/json")
    public GetAllItemsComboboxItemsResponseBean getAllItemsComboboxItems(
            @RequestParam(required = true) Integer typeId,
            @RequestParam(required = false, defaultValue = "false") Boolean includeInActive,
            @RequestParam(required = false, defaultValue = "false") Boolean includeNotAssign) {

        if (typeId == null) {
            throw new IllegalArgumentException("typeId parameter is required.");
        }

        return lookupsService.getAllItemsComboboxItems(typeId, includeInActive, includeNotAssign);
    }
}

