package com.beans.loyalty;

import com.beans.interfaces.ResponsePayload;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Response bean for GetExternalLoyaltiesWithAllowRedeemCombobox API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GetExternalLoyaltiesWithAllowRedeemComboboxResponseBean(
        @JsonProperty("result") List<ExternalLoyaltyWithAllowRedeem> result,
        @JsonProperty("targetUrl") String targetUrl,
        @JsonProperty("success") Boolean success,
        @JsonProperty("error") Object error,
        @JsonProperty("unAuthorizedRequest") Boolean unAuthorizedRequest,
        @JsonProperty("__abp") Boolean abp
) implements ResponsePayload {
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ExternalLoyaltyWithAllowRedeem(
            @JsonProperty("integrationId") String integrationId,
            @JsonProperty("displayText") String displayText,
            @JsonProperty("number") String number,
            @JsonProperty("verificationMethod") Integer verificationMethod,
            @JsonProperty("externalLoyaltyId") Integer externalLoyaltyId
    ) {
    }
}

