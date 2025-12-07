package com.controllers;

import com.beans.GetAllItemsComboboxItemsResponseBean;
import com.services.LookupsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.controllers.ApiPaths.*;

/**
 * Controller for lookup-related endpoints.
 */
@RestController
@RequestMapping(path = BASE_PATH)
public class LookupsController {

    @Autowired
    private LookupsService lookupsService;

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

//    /**
//     * Get all items for a combobox based on the lookup type.
//     * This endpoint automatically calls the authorization-service to get the refreshToken
//     * and uses it in the Authorization header when calling the external API.
//     *
//     * @param typeId The type ID for the lookup items (required).
//     * @param selectedItemId The selected item ID (optional).
//     * @param includeInActive Whether to include inactive items (default: false).
//     * @param includeNotAssign Whether to include not assigned items (default: false).
//     * @return The response containing all combobox items.
//     */
//    @GetMapping(path = CUSTOMER_GET_ALL_ITEMS_COMBOBOX, produces = "application/json")
//    public GetAllItemsComboboxItemsResponseBean getAllItemsComboboxItems(
//            @RequestParam(required = true) Integer typeId,
//            @RequestParam(required = false) Integer selectedItemId,
//            @RequestParam(required = false, defaultValue = "false") Boolean includeInActive,
//            @RequestParam(required = false, defaultValue = "false") Boolean includeNotAssign) {
//
//        if (typeId == null) {
//            throw new IllegalArgumentException("typeId parameter is required.");
//        }
//
//        return customerService.createOrUpdateCustomer(typeId, selectedItemId, includeInActive, includeNotAssign);
//    }
}

