package com.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Request bean for CreateOrUpdateCustomer API.
 */
public record CreateOrUpdateCustomerRequestBean(
        @JsonProperty("customer") CustomerData customer
) {
    public record CustomerData(
            @JsonProperty("fullName") FullName fullName,
            @JsonProperty("contactInformation") ContactInformation contactInformation,
            @JsonProperty("basicInformation") BasicInformation basicInformation,
            @JsonProperty("professionalInformation") ProfessionalInformation professionalInformation,
            @JsonProperty("address") Address address,
            @JsonProperty("secondaryAddress") SecondaryAddress secondaryAddress,
            @JsonProperty("emergencyContacts") List<EmergencyContact> emergencyContacts,
            @JsonProperty("personalPhoto") PersonalPhoto personalPhoto,
            @JsonProperty("documents") List<DocumentDto> documents,
            @JsonProperty("externalLoyalties") List<Object> externalLoyalties,
            @JsonProperty("sourceId") Integer sourceId
    ) {
    }

    public record FullName(
            @JsonProperty("first") String first,
            @JsonProperty("second") String second,
            @JsonProperty("family") String family
    ) {
    }

    public record ContactInformation(
            @JsonProperty("primaryPhone") String primaryPhone,
            @JsonProperty("email") String email
    ) {
    }

    public record BasicInformation(
            @JsonProperty("nationalityId") String nationalityId,
            @JsonProperty("genderId") String genderId,
            @JsonProperty("dateOfBirth") String dateOfBirth
    ) {
    }

    public record ProfessionalInformation(
            // Empty object - fields are nullable to support empty JSON objects
            @JsonProperty("organizationId") String organizationId,
            @JsonProperty("organizationName") String organizationName,
            @JsonProperty("occupationId") String occupationId
    ) {
    }

    public record Address(
            @JsonProperty("countryId") String countryId,
            @JsonProperty("cityId") Integer cityId
    ) {
    }

    public record SecondaryAddress(
            // Empty object - fields are nullable to support empty JSON objects
            @JsonProperty("countryId") String countryId,
            @JsonProperty("cityId") Integer cityId,
            @JsonProperty("details") String details
    ) {
    }

    public record EmergencyContact(
            // Structure can be defined based on actual requirements
            @JsonProperty("name") String name,
            @JsonProperty("phone") String phone,
            @JsonProperty("relationship") String relationship
    ) {
    }

    public record PersonalPhoto(
            @JsonProperty("id") String id,
            @JsonProperty("url") String url,
            @JsonProperty("size") Integer size,
            @JsonProperty("type") String type
    ) {
    }

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
    }

    public record Attachment(
            @JsonProperty("url") String url,
            @JsonProperty("size") Integer size,
            @JsonProperty("type") String type
    ) {
    }
}

