package com.beans.customer;

import com.beans.interfaces.RequestPayload;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Request bean for CreateOrUpdateCustomer API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateOrUpdateCustomerRequestBean(
        @JsonProperty("customer") CustomerData customer
) implements RequestPayload {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CustomerData(
            @JsonProperty("id") Integer id,
            @JsonProperty("fullName") FullName fullName,
            @JsonProperty("contactInformation") ContactInformation contactInformation,
            @JsonProperty("basicInformation") BasicInformation basicInformation,
            @JsonProperty("professionalInformation") ProfessionalInformation professionalInformation,
            @JsonProperty("customerPolicyVerification") CustomerPolicyVerification customerPolicyVerification,
            @JsonProperty("address") Address address,
            @JsonProperty("secondaryAddress") SecondaryAddress secondaryAddress,
            @JsonProperty("emergencyContacts") List<EmergencyContact> emergencyContacts,
            @JsonProperty("personalPhoto") PersonalPhoto personalPhoto,
            @JsonProperty("documents") List<DocumentDto> documents,
            @JsonProperty("externalLoyalties") List<Object> externalLoyalties,
            @JsonProperty("sourceId") Integer sourceId
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record FullName(
            @JsonProperty("first") String first,
            @JsonProperty("second") String second,
            @JsonProperty("family") String family
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ContactInformation(
            @JsonProperty("primaryPhone") String primaryPhone,
            @JsonProperty("email") String email
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record BasicInformation(
            @JsonProperty("nationalityId") String nationalityId,
            @JsonProperty("genderId") String genderId,
            @JsonProperty("dateOfBirth") String dateOfBirth
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ProfessionalInformation(
            // Empty object - fields are nullable to support empty JSON objects
            @JsonProperty("organizationId") String organizationId,
            @JsonProperty("organizationName") String organizationName,
            @JsonProperty("occupationId") String occupationId
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Address(
            @JsonProperty("countryId") String countryId,
            @JsonProperty("cityId") Integer cityId
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record SecondaryAddress(
            // Empty object - fields are nullable to support empty JSON objects
            @JsonProperty("countryId") String countryId,
            @JsonProperty("cityId") Integer cityId,
            @JsonProperty("details") String details
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record EmergencyContact(
            // Structure can be defined based on actual requirements
            @JsonProperty("name") String name,
            @JsonProperty("phone") String phone,
            @JsonProperty("relationship") String relationship
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PersonalPhoto(
            @JsonProperty("id") String id,
            @JsonProperty("url") String url,
            @JsonProperty("size") Integer size,
            @JsonProperty("type") String type
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record DocumentDto(
            @JsonProperty("discriminator") String discriminator,
            @JsonProperty("issueCountryId") String issueCountryId,
            @JsonProperty("typeId") String typeId,
            @JsonProperty("number") String number,
            @JsonProperty("copyNumber") Integer copyNumber,
            @JsonProperty("issueDate") String issueDate,
            @JsonProperty("expiryDate") String expiryDate,
            @JsonProperty("typeName") String typeName,
            @JsonProperty("issueCountry") String issueCountry,
            @JsonProperty("attachment") Attachment attachment,
            // DriverLicenseDto specific fields
            @JsonProperty("licenseCategoryId") String licenseCategoryId
    ) {
        /**
         * Constructor that creates DocumentDto without attachment (sets it to null).
         *
         * @param discriminator The document discriminator type
         * @param issueCountryId The issue country ID
         * @param typeId The document type ID
         * @param number The document number
         * @param copyNumber The copy number
         * @param issueDate The issue date
         * @param expiryDate The expiry date
         * @param typeName The document type name
         * @param issueCountry The issue country name
         * @param licenseCategoryId The license category ID (for DriverLicenseDto)
         */
        public DocumentDto(
                String discriminator,
                String issueCountryId,
                String typeId,
                String number,
                Integer copyNumber,
                String issueDate,
                String expiryDate,
                String typeName,
                String issueCountry,
                String licenseCategoryId
        ) {
            this(discriminator, issueCountryId, typeId, number, copyNumber, issueDate, expiryDate, typeName, issueCountry, null, licenseCategoryId);
        }

        /**
         * Constructor that creates DocumentDto without attachment (sets it to null).
         *
         * @param discriminator The document discriminator type
         * @param number The document number
         * @param copyNumber The copy number
         * @param issueDate The issue date
         * @param expiryDate The expiry date
         * @param typeName The document type name
         * @param issueCountry The issue country name
         * @param licenseCategoryId The license category ID (for DriverLicenseDto)
         */
        public DocumentDto(
                String discriminator,
                String number,
                Integer copyNumber,
                String issueDate,
                String expiryDate,
                String typeName,
                String issueCountry,
                String licenseCategoryId
        ) {
            this(discriminator, null, null, number, copyNumber, issueDate, expiryDate, typeName, issueCountry, null, licenseCategoryId);
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Attachment(
            @JsonProperty("url") String url,
            @JsonProperty("size") Integer size,
            @JsonProperty("type") String type
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.ALWAYS)
    public record CustomerPolicyVerification(
            @JsonProperty("isTermsConditionsAndPrivacyPolicyApproved") Boolean isTermsConditionsAndPrivacyPolicyApproved,
            @JsonProperty("isMarketingMaterialsApproved") Boolean isMarketingMaterialsApproved,
            @JsonProperty("isDataSharingPolicyApproved") Boolean isDataSharingPolicyApproved
    ) {
    }
}

