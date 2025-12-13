package com.beans.customer;

import com.beans.interfaces.ResponsePayload;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Response bean for GetCustomerContractInformationByName API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GetCustomerContractInformationByNameResponseBean(
        @JsonProperty("result") List<CustomerContractInformation> result,
        @JsonProperty("targetUrl") String targetUrl,
        @JsonProperty("success") Boolean success,
        @JsonProperty("error") Object error,
        @JsonProperty("unAuthorizedRequest") Boolean unAuthorizedRequest,
        @JsonProperty("__abp") Boolean abp
) implements ResponsePayload {
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CustomerContractInformation(
            @JsonProperty("id") Integer id,
            @JsonProperty("fullName") String fullName,
            @JsonProperty("primaryPhone") String primaryPhone,
            @JsonProperty("secondaryPhone") String secondaryPhone,
            @JsonProperty("organization") String organization,
            @JsonProperty("membership") String membership,
            @JsonProperty("email") String email,
            @JsonProperty("vipLevel") String vipLevel,
            @JsonProperty("membershipLevelId") Integer membershipLevelId,
            @JsonProperty("identityInfo") IdentityInfo identityInfo,
            @JsonProperty("isVerifiedEmail") Boolean isVerifiedEmail,
            @JsonProperty("isCustomerMembershipBlocked") Boolean isCustomerMembershipBlocked,
            @JsonProperty("liteCustomerId") Integer liteCustomerId
    ) {
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record IdentityInfo(
            @JsonProperty("identityId") Integer identityId,
            @JsonProperty("identityNumber") String identityNumber,
            @JsonProperty("copyNumber") String copyNumber
    ) {
    }
}

