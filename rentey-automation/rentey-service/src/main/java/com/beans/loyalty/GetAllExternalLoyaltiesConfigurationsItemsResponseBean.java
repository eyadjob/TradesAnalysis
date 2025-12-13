package com.beans.loyalty;

import com.beans.interfaces.ResponsePayload;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Response bean for GetAllExternalLoyaltiesConfigurationsItems API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GetAllExternalLoyaltiesConfigurationsItemsResponseBean(
        @JsonProperty("result") ExternalLoyaltiesConfigurationsResult result,
        @JsonProperty("targetUrl") String targetUrl,
        @JsonProperty("success") Boolean success,
        @JsonProperty("error") Object error,
        @JsonProperty("unAuthorizedRequest") Boolean unAuthorizedRequest,
        @JsonProperty("__abp") Boolean abp
) implements ResponsePayload {
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ExternalLoyaltiesConfigurationsResult(
            @JsonProperty("items") List<ExternalLoyaltyConfigurationItem> items
    ) {
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ExternalLoyaltyConfigurationItem(
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

