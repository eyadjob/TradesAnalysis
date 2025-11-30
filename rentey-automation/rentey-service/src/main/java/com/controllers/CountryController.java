package com.controllers;

import com.beans.GetCountryCurrencyInfoResponseBean;
import com.beans.GetUserBranchesForComboboxResponseBean;
import com.services.CountryService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static com.controllers.ApiPaths.*;

@RestController
@RequestMapping(path = BASE_PATH)
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

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

        return countryService.getUserBranchesForCombobox(includeInActive, countryId, includeAll, filterTypes);
    }
}

