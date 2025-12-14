package com.controllers;

import com.beans.general.GetAllItemsComboboxItemsResponseBean;
import com.beans.lookups.GetItemsByTypeResponseBean;
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

    /**
     * Get items by type.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param typeId The type ID for the lookup items (required).
     * @param includeInActive Whether to include inactive items (default: false).
     * @return The response containing all items for the specified type.
     */
    @GetMapping(path = LOOKUPS_GET_ITEMS_BY_TYPE, produces = "application/json")
    public GetItemsByTypeResponseBean getItemsByType(
            @RequestParam(required = true) Integer typeId,
            @RequestParam(required = false, defaultValue = "false") Boolean includeInActive) {

        if (typeId == null) {
            throw new IllegalArgumentException("typeId parameter is required.");
        }

        return lookupsService.getItemsByType(typeId, includeInActive);
    }

    /**
     * Get payment methods combobox items.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param includeInActive Whether to include inactive items (default: false).
     * @param includeNotAssign Whether to include not assigned items (default: true).
     * @return The response containing payment methods combobox items.
     */
    @GetMapping(path = PAYMENT_GET_PAYMENT_METHODS_COMBOBOX_ITEMS, produces = "application/json")
    public GetAllItemsComboboxItemsResponseBean getPaymentMethodsComboboxItems(
            @RequestParam(required = false, defaultValue = "false") Boolean includeInActive,
            @RequestParam(required = false, defaultValue = "true") Boolean includeNotAssign) {
        return lookupsService.getPaymentMethodsComboboxItems(includeInActive, includeNotAssign);
    }
}

