package com.services;

import com.beans.GetAllItemsComboboxItemsResponseBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class LookupsService {

    private final WebClient settingsWebClient;
    private final String apiBasePath;

    public LookupsService(
            @Qualifier("settingsWebClient") WebClient settingsWebClient,
            @Qualifier("apiBasePath") String apiBasePath) {
        this.settingsWebClient = settingsWebClient;
        this.apiBasePath = apiBasePath;
    }

    /**
     * Get all items for a combobox based on the lookup type.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param typeId The type ID for the lookup items.
     * @param includeInActive Whether to include inactive items.
     * @param includeNotAssign Whether to include not assigned items.
     * @return The response containing all combobox items.
     */
    public GetAllItemsComboboxItemsResponseBean getAllItemsComboboxItems(
            Integer typeId,
            Boolean includeInActive,
            Boolean includeNotAssign) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(apiBasePath + "/Lookups/GetAllItemsComboboxItems")
                        .queryParam("typeId", typeId)
                        .queryParam("includeInActive", includeInActive)
                        .queryParam("includeNotAssign", includeNotAssign)
                        .build())
                .retrieve()
                .bodyToMono(GetAllItemsComboboxItemsResponseBean.class)
                .block();
    }
}

