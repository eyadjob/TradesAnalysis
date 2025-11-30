package com.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Response bean for CreateOrUpdateCustomer API.
 */
public record CreateOrUpdateCustomerResponseBean(
        @JsonProperty("result") CustomerResult result,
        @JsonProperty("targetUrl") String targetUrl,
        @JsonProperty("success") Boolean success,
        @JsonProperty("error") String error,
        @JsonProperty("unAuthorizedRequest") Boolean unAuthorizedRequest,
        @JsonProperty("__abp") Boolean abp
) {
    public record CustomerResult(
            @JsonProperty("guid") String guid,
            @JsonProperty("fullName") CustomerFullName fullName,
            @JsonProperty("contactInformation") CustomerContactInformation contactInformation,
            @JsonProperty("basicInformation") CustomerBasicInformation basicInformation,
            @JsonProperty("customerPolicyVerification") CustomerPolicyVerification customerPolicyVerification,
            @JsonProperty("professionalInformation") CustomerProfessionalInformation professionalInformation,
            @JsonProperty("address") CustomerAddress address,
            @JsonProperty("secondaryAddress") CustomerSecondaryAddress secondaryAddress,
            @JsonProperty("emergencyContacts") List<Object> emergencyContacts,
            @JsonProperty("personalPhoto") CustomerPersonalPhoto personalPhoto,
            @JsonProperty("documents") List<CustomerDocument> documents,
            @JsonProperty("externalLoyalties") List<Object> externalLoyalties,
            @JsonProperty("isIntegrated") Boolean isIntegrated,
            @JsonProperty("notes") String notes,
            @JsonProperty("sourceId") Integer sourceId,
            @JsonProperty("customerProvidersIntegrationResult") CustomerProvidersIntegrationResult customerProvidersIntegrationResult,
            @JsonProperty("employeeId") Integer employeeId,
            @JsonProperty("liteCustomerId") Long liteCustomerId,
            @JsonProperty("registrationBranchId") Integer registrationBranchId,
            @JsonProperty("termsConditionsAndPrivacyPolicyDate") String termsConditionsAndPrivacyPolicyDate,
            @JsonProperty("marketingMaterialsDate") String marketingMaterialsDate,
            @JsonProperty("dataSharingPolicyDate") String dataSharingPolicyDate,
            @JsonProperty("membershipNumber") String membershipNumber,
            @JsonProperty("isDeleted") Boolean isDeleted,
            @JsonProperty("deleterUserId") Integer deleterUserId,
            @JsonProperty("deletionTime") String deletionTime,
            @JsonProperty("lastModificationTime") String lastModificationTime,
            @JsonProperty("lastModifierUserId") Integer lastModifierUserId,
            @JsonProperty("creationTime") String creationTime,
            @JsonProperty("creatorUserId") Integer creatorUserId,
            @JsonProperty("id") Long id
    ) {
    }

    public record CustomerFullName(
            @JsonProperty("first") String first,
            @JsonProperty("second") String second,
            @JsonProperty("third") String third,
            @JsonProperty("fourth") String fourth,
            @JsonProperty("family") String family,
            @JsonProperty("displayName") String displayName
    ) {
    }

    public record CustomerContactInformation(
            @JsonProperty("primaryPhone") String primaryPhone,
            @JsonProperty("secondaryPhone") String secondaryPhone,
            @JsonProperty("email") String email,
            @JsonProperty("isVerifiedEmail") Boolean isVerifiedEmail,
            @JsonProperty("hasPermissionToSaveInvalidEmail") Boolean hasPermissionToSaveInvalidEmail,
            @JsonProperty("isVerifiedSecondaryNumber") Boolean isVerifiedSecondaryNumber
    ) {
    }

    public record CustomerBasicInformation(
            @JsonProperty("nationalityId") Integer nationalityId,
            @JsonProperty("genderId") Integer genderId,
            @JsonProperty("maritalStatusId") Integer maritalStatusId,
            @JsonProperty("noOfDependencies") Integer noOfDependencies,
            @JsonProperty("dateOfBirth") String dateOfBirth,
            @JsonProperty("vipLevelId") Integer vipLevelId
    ) {
    }

    public record CustomerPolicyVerification(
            @JsonProperty("isTermsConditionsAndPrivacyPolicyApproved") Boolean isTermsConditionsAndPrivacyPolicyApproved,
            @JsonProperty("isMarketingMaterialsApproved") Boolean isMarketingMaterialsApproved,
            @JsonProperty("isDataSharingPolicyApproved") Boolean isDataSharingPolicyApproved
    ) {
    }

    public record CustomerProfessionalInformation(
            @JsonProperty("organizationId") Integer organizationId,
            @JsonProperty("organizationName") String organizationName,
            @JsonProperty("occupationId") Integer occupationId
    ) {
    }

    public record CustomerAddress(
            @JsonProperty("countryId") Integer countryId,
            @JsonProperty("cityId") Integer cityId,
            @JsonProperty("details") String details
    ) {
    }

    public record CustomerSecondaryAddress(
            @JsonProperty("countryId") Integer countryId,
            @JsonProperty("cityId") Integer cityId,
            @JsonProperty("details") String details
    ) {
    }

    public record CustomerPersonalPhoto(
            @JsonProperty("id") String id,
            @JsonProperty("url") String url,
            @JsonProperty("size") Double size,
            @JsonProperty("type") String type
    ) {
    }

    public record CustomerDocument(
            @JsonProperty("issueCountryId") Integer issueCountryId,
            @JsonProperty("typeId") Integer typeId,
            @JsonProperty("number") String number,
            @JsonProperty("copyNumber") String copyNumber,
            @JsonProperty("issueDate") String issueDate,
            @JsonProperty("expiryDate") String expiryDate,
            @JsonProperty("discriminator") String discriminator,
            @JsonProperty("typeName") String typeName,
            @JsonProperty("issueCountry") String issueCountry,
            @JsonProperty("attachment") CustomerAttachment attachment,
            @JsonProperty("customerId") Long customerId,
            @JsonProperty("tenantId") Integer tenantId,
            @JsonProperty("isDeleted") Boolean isDeleted,
            @JsonProperty("deleterUserId") Integer deleterUserId,
            @JsonProperty("deletionTime") String deletionTime,
            @JsonProperty("lastModificationTime") String lastModificationTime,
            @JsonProperty("lastModifierUserId") Integer lastModifierUserId,
            @JsonProperty("creationTime") String creationTime,
            @JsonProperty("creatorUserId") Integer creatorUserId,
            @JsonProperty("id") Long id,
            // DriverLicenseDto specific fields
            @JsonProperty("licenseCategoryId") Integer licenseCategoryId
    ) {
    }

    public record CustomerAttachment(
            @JsonProperty("id") String id,
            @JsonProperty("url") String url,
            @JsonProperty("size") Double size,
            @JsonProperty("type") String type
    ) {
    }

    public record CustomerProvidersIntegrationResult(
            @JsonProperty("isUiFieldsDisabled") Boolean isUiFieldsDisabled,
            @JsonProperty("isIntegrationSuccess") Boolean isIntegrationSuccess,
            @JsonProperty("isVisitor") Boolean isVisitor,
            @JsonProperty("errorMessage") String errorMessage,
            @JsonProperty("canProceed") Boolean canProceed,
            @JsonProperty("errorCode") String errorCode
    ) {
    }
}

