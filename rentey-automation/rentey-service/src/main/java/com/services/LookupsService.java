package com.services;

import com.annotation.LogExecutionTime;
import com.annotation.LogRequestAndResponseOnDesk;
import com.beans.GetAllItemsComboboxItemsResponseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Service for lookup-related operations.
 * All methods automatically include Authorization header via AuthorizationHeaderFilter.
 */
@Service
public class LookupsService {

    @Autowired
    @Qualifier("settingsWebClient")
    private WebClient settingsWebClient;

    @Autowired
    @Qualifier("apiBasePath")
    private String apiBasePath;

    /**
     * Get all types for combobox items.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @return The response containing all types combobox items.
     */
    @LogRequestAndResponseOnDesk
    @LogExecutionTime
    public GetAllItemsComboboxItemsResponseBean getTypesComboboxItems() {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(apiBasePath + "/Lookups/GetTypesComboboxItems")
                .retrieve()
                .bodyToMono(GetAllItemsComboboxItemsResponseBean.class)
                .block();
    }
}
