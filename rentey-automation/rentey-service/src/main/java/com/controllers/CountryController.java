package com.controllers;

import com.beans.general.GetAllItemsComboboxItemsResponseBean;
import com.beans.country.GetCountryCurrencyInfoResponseBean;
import com.beans.country.GetCurrenciesForComboboxResponseBean;
import com.beans.user.GetUserBranchesForComboboxResponseBean;
import com.services.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static com.controllers.ApiPaths.*;

@RestController
@RequestMapping(path = BASE_PATH)
public class CountryController {

    @Autowired
    private CountryService countryService;

    /**
     * Get country currency information by country ID.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param countryId The country ID for which to get the currency information (required).
     * @return The response containing the country currency information.
     */

    @GetMapping(path = CURRENCY_GET_COUNTRY_CURRENCY_INFO, produces = "application/json")
    public GetCountryCurrencyInfoResponseBean getCountryCurrencyInfo(
            @RequestParam(required = true) Integer countryId) {

        if (countryId == null) {
            throw new IllegalArgumentException("countryId parameter is required.");
        }

        return countryService.getCountryCurrencyInfo(countryId);
    }

    /**
     * Get user branches for combobox.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param includeInActive Whether to include inactive branches (default: false).
     * @param countryId The country ID for which to get the branches (required).
     * @param includeAll Whether to include all branches (default: false).
     * @param filterTypes List of filter types to apply (optional).
     * @return The response containing all user branches for combobox.
     */
    @GetMapping(path = BRANCH_GET_USER_BRANCHES_FOR_COMBOBOX, produces = "application/json")
    public GetUserBranchesForComboboxResponseBean getUserBranchesForCombobox(
            @RequestParam(required = false, defaultValue = "false") Boolean includeInActive,
            @RequestParam(required = true) Integer countryId,
            @RequestParam(required = false, defaultValue = "false") Boolean includeAll,
            @RequestParam(required = false) List<Integer> filterTypes) {

        if (countryId == null) {
            throw new IllegalArgumentException("countryId parameter is required.");
        }

        return countryService.getUserBranchesForCombobox(countryId, includeInActive, includeAll, filterTypes);
    }

    /**
     * Get countries for combobox.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param includeInActive Whether to include inactive countries (default: false).
     * @param includeNotAssign Whether to include "Not assigned" option (default: true).
     * @return The response containing all countries for combobox.
     */
    @GetMapping(path = COUNTRY_GET_COUNTRIES_FOR_COMBOBOX, produces = "application/json")
    public GetAllItemsComboboxItemsResponseBean getCountriesForCombobox(
            @RequestParam(required = false, defaultValue = "false") Boolean includeInActive,
            @RequestParam(required = false, defaultValue = "true") Boolean includeNotAssign) {

        return countryService.getCountriesForCombobox(includeInActive, includeNotAssign);
    }

    /**
     * Get currencies for combobox.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param includeInActive Whether to include inactive currencies (default: false).
     * @return The response containing all currencies for combobox.
     */
    @GetMapping(path = CURRENCY_GET_CURRENCIES_FOR_COMBOBOX, produces = "application/json")
    public GetCurrenciesForComboboxResponseBean getCurrenciesForCombobox(
            @RequestParam(required = false, defaultValue = "false") Boolean includeInActive) {

        return countryService.getCurrenciesForCombobox(includeInActive);
    }

    /**
     * Get branches countries combobox items.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @return The response containing all branches countries combobox items.
     */
    @GetMapping(path = BRANCH_GET_BRANCHES_COUNTRIES_COMBOBOX_ITEMS, produces = "application/json")
    public GetAllItemsComboboxItemsResponseBean getBranchesCountriesComboboxItems() {
        return countryService.getBranchesCountriesComboboxItems();
    }

    /**
     * Get nationalities for combobox.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param includeInActive Whether to include inactive nationalities (default: false).
     * @return The response containing all nationalities for combobox.
     */
    @GetMapping(path = COUNTRY_GET_NATIONALITIES_FOR_COMBOBOX, produces = "application/json")
    public GetAllItemsComboboxItemsResponseBean getNationalitiesForCombobox(
            @RequestParam(required = false, defaultValue = "false") Boolean includeInActive) {
        return countryService.getNationalitiesForCombobox(includeInActive);
    }
}

