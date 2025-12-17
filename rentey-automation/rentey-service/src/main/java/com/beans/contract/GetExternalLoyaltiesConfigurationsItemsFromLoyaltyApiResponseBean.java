package com.beans.contract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Response bean for GetExternalLoyaltiesConfigurationsItems from Loyalty API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GetExternalLoyaltiesConfigurationsItemsFromLoyaltyApiResponseBean(
        @JsonProperty("items") List<LoyaltyConfigurationItem> items
) {
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record LoyaltyConfigurationItem(
            @JsonProperty("id") Integer id,
            @JsonProperty("integratedLoyaltyId") String integratedLoyaltyId,
            @JsonProperty("displayText") String displayText,
            @JsonProperty("isSelected") Boolean isSelected,
            @JsonProperty("allowMandatoryVerification") Boolean allowMandatoryVerification,
            @JsonProperty("allowVerificationWhileAddingToTheCustomer") Boolean allowVerificationWhileAddingToTheCustomer,
            @JsonProperty("allowEran") Boolean allowEran,
            @JsonProperty("allowRedeem") Boolean allowRedeem,
            @JsonProperty("verificationMethod") Integer verificationMethod
    ) {
    }
}
