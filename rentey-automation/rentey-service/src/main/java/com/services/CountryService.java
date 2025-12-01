package com.services;

import com.beans.GetCountryCurrencyInfoResponseBean;
import com.beans.GetUserBranchesForComboboxResponseBean;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CountryService {

    private final WebClient settingsWebClient;

    public CountryService(
            @Qualifier("settingsWebClient") WebClient settingsWebClient) {
        this.settingsWebClient = settingsWebClient;
    }

    /**
     * Get country currency information by country ID.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param countryId The country ID for which to get the currency information.
     * @return The response containing the country currency information.
     */
    public GetCountryCurrencyInfoResponseBean getCountryCurrencyInfo(Integer countryId) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/webapigw/api/services/app/Currency/GetCountryCurrencyInfo")
                        .queryParam("countryId", countryId)
                        .build())
                .retrieve()
                .bodyToMono(GetCountryCurrencyInfoResponseBean.class)
                .block();
    }

    /**
     * Get user branches for combobox.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param includeInActive Whether to include inactive branches.
     * @param countryId The country ID for which to get the branches.
     * @param includeAll Whether to include all branches.
     * @param filterTypes List of filter types to apply.
     * @return The response containing all user branches for combobox.
     */
    public GetUserBranchesForComboboxResponseBean getUserBranchesForCombobox(
            Boolean includeInActive,
            Integer countryId,
            Boolean includeAll,
            List<Integer> filterTypes) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder
                            .path("/webapigw/api/services/app/Branch/GetUserBranchesForCombobox");
                    
                    if (includeInActive != null) {
                        builder.queryParam("includeInActive", includeInActive);
                    }
                    if (countryId != null) {
                        builder.queryParam("countryId", countryId);
                    }
                    if (includeAll != null) {
                        builder.queryParam("includeAll", includeAll);
                    }
                    if (filterTypes != null && !filterTypes.isEmpty()) {
                        for (Integer filterType : filterTypes) {
                            builder.queryParam("filterTypes", filterType);
                        }
                    }
                    
                    return builder.build();
                })
                .retrieve()
                .bodyToMono(GetUserBranchesForComboboxResponseBean.class)
                .block();
    }
}

