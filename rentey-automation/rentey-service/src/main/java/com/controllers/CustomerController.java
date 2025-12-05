package com.controllers;

import com.beans.CreateOrUpdateCustomerRequestBean;
import com.beans.CreateOrUpdateCustomerResponseBean;
import com.beans.GetAllItemsComboboxItemsResponseBean;
import com.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.controllers.ApiPaths.*;

@RestController
@RequestMapping(path = BASE_PATH)
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**
     * Create or update a customer.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param request The request containing customer information.
     * @return The response containing the created or updated customer data.
     */
    @PostMapping(path = CUSTOMER_CREATE_OR_UPDATE, consumes = "application/json", produces = "application/json")
    public CreateOrUpdateCustomerResponseBean createOrUpdateCustomer(
            @RequestBody(required = true) CreateOrUpdateCustomerRequestBean request) {

        if (request == null) {
            throw new IllegalArgumentException("Request body is required and cannot be null.");
        }

        if (request.customer() == null) {
            throw new IllegalArgumentException("Customer data is required and cannot be null.");
        }

        return customerService.createOrUpdateCustomer(request);
    }

    /**
     * Get all items for a combobox based on the lookup type.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param typeId The type ID for the lookup items (required).
     * @param selectedItemId The selected item ID (optional).
     * @param includeInActive Whether to include inactive items (default: false).
     * @param includeNotAssign Whether to include not assigned items (default: false).
     * @return The response containing all combobox items.
     */
    @GetMapping(path = CUSTOMER_GET_ALL_ITEMS_COMBOBOX, produces = "application/json")
    public GetAllItemsComboboxItemsResponseBean getAllItemsComboboxItems(
            @RequestParam(required = true) Integer typeId,
            @RequestParam(required = false) Integer selectedItemId,
            @RequestParam(required = false, defaultValue = "false") Boolean includeInActive,
            @RequestParam(required = false, defaultValue = "false") Boolean includeNotAssign) {

        if (typeId == null) {
            throw new IllegalArgumentException("typeId parameter is required.");
        }

        return customerService.getAllItemsComboboxItems(typeId, selectedItemId, includeInActive, includeNotAssign);
    }
}

