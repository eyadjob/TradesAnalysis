package com.controllers;

import com.beans.GetCountriesPhoneResponseBean;
import com.services.ContractService;
import org.springframework.web.bind.annotation.*;

import static com.controllers.ApiPaths.*;

@RestController
@RequestMapping(path = BASE_PATH)
public class ContractController {

    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

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
}

