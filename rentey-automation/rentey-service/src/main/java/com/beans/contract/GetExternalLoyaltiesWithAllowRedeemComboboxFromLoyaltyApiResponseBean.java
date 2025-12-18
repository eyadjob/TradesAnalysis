package com.beans.contract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response bean for GetExternalLoyaltiesWithAllowRedeemCombobox from Loyalty API.
 * This is a direct array response (not wrapped in a result object).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GetExternalLoyaltiesWithAllowRedeemComboboxFromLoyaltyApiResponseBean(
        @JsonProperty("integrationId") String integrationId,
        @JsonProperty("displayText") String displayText,
        @JsonProperty("number") String number,
        @JsonProperty("verificationMethod") Integer verificationMethod,
        @JsonProperty("externalLoyaltyId") Integer externalLoyaltyId
) {
}
