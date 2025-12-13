package com.controllers;

import com.beans.contract.GetExtrasNamesExcludedFromBookingPaymentDetailsResponseBean;
import com.beans.customer.GetCountriesPhoneResponseBean;
import com.services.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.controllers.ApiPaths.*;

@RestController
@RequestMapping(path = BASE_PATH)
public class ContractController {

    @Autowired
    private ContractService contractService;

    /**
     * Get countries phone information.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param typeId The type ID for filtering countries (required).
     * @param includeInActive Whether to include inactive countries (default: false).
     * @param includeNotAssign Whether to include not assigned countries (default: false).
     * @return The response containing all countries phone information.
     */
    @GetMapping(path = COUNTRY_GET_COUNTRIES_PHONE, produces = "application/json")
    public GetCountriesPhoneResponseBean getCountriesPhone(
            @RequestParam(required = true) Integer typeId,
            @RequestParam(required = false, defaultValue = "false") Boolean includeInActive,
            @RequestParam(required = false, defaultValue = "false") Boolean includeNotAssign) {

        if (typeId == null) {
            throw new IllegalArgumentException("typeId parameter is required.");
        }

        return contractService.getCountriesPhone(typeId, includeInActive, includeNotAssign);
    }

    /**
     * Get extras names excluded from booking payment details.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @return The response containing a list of extras names excluded from booking payment details.
     */
    @GetMapping(path = CONTRACT_EXTRA_CONFIGURATION_GET_EXTRAS_NAMES_EXCLUDED, produces = "application/json")
    public GetExtrasNamesExcludedFromBookingPaymentDetailsResponseBean getExtrasNamesExcludedFromBookingPaymentDetails() {
        return contractService.getExtrasNamesExcludedFromBookingPaymentDetails();
    }
}

