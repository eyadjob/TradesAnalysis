package com.services;

import com.beans.GetCountriesPhoneResponseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ContractService {

    @Autowired
    @Qualifier("settingsWebClient")
    private WebClient settingsWebClient;

    @Autowired
    @Qualifier("apiBasePath")
    private String apiBasePath;

    /**
     * Get countries phone information.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param typeId The type ID for filtering countries (required).
     * @param includeInActive Whether to include inactive countries (default: false).
     * @param includeNotAssign Whether to include not assigned countries (default: false).
     * @return The response containing all countries phone information.
     */
    public GetCountriesPhoneResponseBean getCountriesPhone(
            Integer typeId,
            Boolean includeInActive,
            Boolean includeNotAssign) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(apiBasePath + "/Country/GetCountriesPhone")
                        .queryParam("typeId", typeId)
                        .queryParam("includeInActive", includeInActive)
                        .queryParam("includeNotAssign", includeNotAssign)
                        .build())
                .retrieve()
                .bodyToMono(GetCountriesPhoneResponseBean.class)
                .block();
    }
}

