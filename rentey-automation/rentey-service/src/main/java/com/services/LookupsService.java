package com.services;

import com.annotation.LogExecutionTime;
import com.annotation.LogRequestAndResponseOnDesk;
import com.beans.general.GetAllItemsComboboxItemsResponseBean;
import com.beans.lookups.GetItemsByTypeResponseBean;
import com.enums.LookupTypes;
import com.util.PropertyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

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

    @Autowired
    private CountryService countryService;

    private GetAllItemsComboboxItemsResponseBean lookupTypes;
    private GetAllItemsComboboxItemsResponseBean nationalities;
    private static final Map<String, String> countryIso = PropertyManager.loadPropertyFileIntoMap("country-iso.properties");

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
    @LogExecutionTime
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
    @LogExecutionTime
    public GetAllItemsComboboxItemsResponseBean getAllItemsComboboxItems(
            Integer typeId) {
        return getAllItemsComboboxItems(typeId,false,false);
    }

    @LogExecutionTime
    public String getLookupItemIdByLookupTypeNameAndItemDisplayName(LookupTypes lookupType, String itemDisplayText) {
        int lookupTypeId = getLookupTypeIdByName(lookupType.name());
        GetAllItemsComboboxItemsResponseBean comboboxItemsResponseBean= getAllItemsComboboxItems(lookupTypeId, false, false);
        return comboboxItemsResponseBean.result().items().stream().filter(i -> i.displayText().equals(itemDisplayText)).map(i -> i.value()).findAny().orElse("-1");

    }

    public String getLookupItemIdByLookupTypeIdAndItemDisplayName(int lookupTypeId, String itemDisplayText) {
        GetAllItemsComboboxItemsResponseBean comboboxItemsResponseBean= getAllItemsComboboxItems(lookupTypeId, false, false);
        return comboboxItemsResponseBean.result().items().stream().filter(i -> i.displayText().equals(itemDisplayText)).map(i -> i.value()).findAny().orElse("-1");

    }


    /**
     * Get items by type.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param typeId The type ID for the lookup items (required).
     * @param includeInActive Whether to include inactive items (default: false).
     * @return The response containing all items for the specified type.
     */
    @Cacheable(cacheNames = "itemsByTypeCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public GetItemsByTypeResponseBean getItemsByType(Integer typeId, Boolean includeInActive) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder
                            .path(apiBasePath + "/Lookups/GetItemsByType");
                    
                    if (typeId != null) {
                        builder.queryParam("typeId", typeId);
                    }
                    if (includeInActive != null) {
                        builder.queryParam("includeInActive", includeInActive);
                    }
                    
                    return builder.build();
                })
                .retrieve()
                .bodyToMono(GetItemsByTypeResponseBean.class)
                .block();
    }

    public String getComboboxItemValueByDisplayText(GetAllItemsComboboxItemsResponseBean comboboxItemsResponseBean,String displayText) {
        for (GetAllItemsComboboxItemsResponseBean.ComboboxItem comboboxItem : comboboxItemsResponseBean.result().items()) {
            if (comboboxItem.displayText().equals(displayText)) {
                return comboboxItem.value();
            }
        }
        return "-1";
    }

    /**
     * Get payment methods combobox items.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param includeInActive Whether to include inactive items (default: false).
     * @param includeNotAssign Whether to include not assigned items (default: true).
     * @return The response containing payment methods combobox items.
     */
    @Cacheable(cacheNames = "paymentMethodsComboboxItemsCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public GetAllItemsComboboxItemsResponseBean getPaymentMethodsComboboxItems(Boolean includeInActive, Boolean includeNotAssign) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder
                            .path(apiBasePath + "/Payment/GetPaymentMethodsComboboxItems");
                    
                    if (includeInActive != null) {
                        builder.queryParam("includeInActive", includeInActive);
                    }
                    if (includeNotAssign != null) {
                        builder.queryParam("includeNotAssign", includeNotAssign);
                    }
                    
                    return builder.build();
                })
                .retrieve()
                .bodyToMono(GetAllItemsComboboxItemsResponseBean.class)
                .block();
    }

    /**
     * Gets the combobox item value by display text for a given type.
     *
     * @param displayText The display text to search for
     * @param typeId The type ID for the lookup items
     * @return The value of the matching combobox item, or null if not found
     */
    public String getComboboxItemsValueByDisplayText(String displayText, int typeId) {
        GetAllItemsComboboxItemsResponseBean comboboxItems = getAllItemsComboboxItems(typeId, false, false);
        for (GetAllItemsComboboxItemsResponseBean.ComboboxItem item : comboboxItems.result().items()) {
            if (displayText != null && displayText.trim().equals(item.displayText())) {
                return item.value();
            }
        }
        return null;
    }

    /**
     * Gets the lookup type ID by name.
     *
     * @param lookupTypeName The name of the lookup type
     * @return The lookup type ID, or -1 if not found
     */
    public int getLookupTypeIdByName(String lookupTypeName) {
        if (this.lookupTypes == null) {
            this.lookupTypes = getTypesComboboxItems();
        }
        for (GetAllItemsComboboxItemsResponseBean.ComboboxItem lookupType : lookupTypes.result().items()) {
            if (lookupTypeName != null && lookupTypeName.trim().equals(lookupType.displayText())) {
                return Integer.parseInt(lookupType.value());
            }
        }
        return -1;
    }

    /**
     * Gets the nationality ID by ISO code.
     *
     * @param nationalityIsoCode The ISO code of the nationality
     * @return The nationality ID, or -1 if not found
     */
    public int getNationalityIdByName(String nationalityIsoCode) {
        if (this.nationalities == null) {
            this.nationalities = countryService.getCountriesForCombobox(false, false);
        }
        String countryName = countryIso.get(nationalityIsoCode);
        if (countryName == null) {
            return -1;
        }
        for (GetAllItemsComboboxItemsResponseBean.ComboboxItem nationality : nationalities.result().items()) {
            if (countryName.equals(nationality.displayText())) {
                return Integer.parseInt(nationality.value());
            }
        }
        return -1;
    }

    /**
     * Gets the country name by ISO code.
     *
     * @param isoCode The ISO code of the country
     * @return The country name, or null if not found
     */
    public String getCountryNameByIsoCode(String isoCode) {
        return countryIso.get(isoCode);
    }

}
