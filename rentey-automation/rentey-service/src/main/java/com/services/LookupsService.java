package com.services;

import com.annotation.LogExecutionTime;
import com.annotation.LogRequestAndResponseOnDesk;
import com.beans.general.GetAllItemsComboboxItemsResponseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable(cacheNames = "typesComboboxItems", keyGenerator = "AutoKeyGenerator")
    public GetAllItemsComboboxItemsResponseBean getTypesComboboxItems() {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(apiBasePath + "/Lookups/GetTypesComboboxItems")
                .retrieve()
                .bodyToMono(GetAllItemsComboboxItemsResponseBean.class)
                .block();
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
    @Cacheable(cacheNames = "allItemsComboboxItems", keyGenerator = "AutoKeyGenerator")
    public GetAllItemsComboboxItemsResponseBean getAllItemsComboboxItems(
            Integer typeId,
            Boolean includeInActive,
            Boolean includeNotAssign) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder
                            .path(apiBasePath + "/Lookups/GetAllItemsComboboxItems")
                            .queryParam("typeId", typeId)
                            .queryParam("includeInActive", includeInActive)
                            .queryParam("includeNotAssign", includeNotAssign);
                    return builder.build();
                })
                .retrieve()
                .bodyToMono(GetAllItemsComboboxItemsResponseBean.class)
                .block();
    }

    /**
     * Get all items for a combobox based on the lookup type.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param typeId The type ID for the lookup items.
     * @return The response containing all combobox items.
     */
    @Cacheable(cacheNames = "allItemsComboboxItems", keyGenerator = "AutoKeyGenerator")
    public GetAllItemsComboboxItemsResponseBean getAllItemsComboboxItems(
            Integer typeId) {
        return getAllItemsComboboxItems(typeId,false,false);
    }


    public String getComboboxItemValueByDisplayText(GetAllItemsComboboxItemsResponseBean comboboxItemsResponseBean,String displayText) {
        for (GetAllItemsComboboxItemsResponseBean.ComboboxItem comboboxItem : comboboxItemsResponseBean.result().items()) {
            if (comboboxItem.displayText().equals(displayText)) {
                return comboboxItem.value();
            }
        }
        return "-1";
    }


}
