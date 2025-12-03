package com.builders;

import com.beans.CreateOrUpdateCustomerRequestBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder class for creating CreateOrUpdateCustomerRequestBean with default values
 * from the provided JSON payload. Allows customization of any field.
 */
public class CustomerDataBuilder {

    // Default values from the JSON payload
    private String firstName = "Eyad Automation Test Customer douehuuoqqyb";
    private String secondName = "Eyad Automation Test Customer douehuuoqqyb";
    private String familyName = "Eyad Automation Test Customer douehuuoqqyb";
    
    private String primaryPhone = "966-517067328";
    private String email = "EyadAutomationTestCustomerdouehuuoqqyb@iyelo.com";
    
    private String nationalityId = "1";
    private String genderId = "110";
    private String dateOfBirth = "1999-08-26T16:14:05+03:00";
    
    private String organizationId = null;
    private String organizationName = null;
    private String occupationId = null;
    
    private String addressCountryId = "1";
    private Integer addressCityId = -1;
    
    private String secondaryAddressCountryId = null;
    private Integer secondaryAddressCityId = null;
    private String secondaryAddressDetails = null;
    
    private List<CreateOrUpdateCustomerRequestBean.EmergencyContact> emergencyContacts = new ArrayList<>();
    
    private String personalPhotoId = "";
    private String personalPhotoUrl = "";
    private Integer personalPhotoSize = 0;
    private String personalPhotoType = "";
    
    private List<CreateOrUpdateCustomerRequestBean.DocumentDto> documents = new ArrayList<>();
    
    private List<Object> externalLoyalties = new ArrayList<>();
    
    private Integer sourceId = 120;
    
    private Integer id = 0;
    private Boolean isTermsConditionsAndPrivacyPolicyApproved = true;
    private Boolean isMarketingMaterialsApproved = null;
    private Boolean isDataSharingPolicyApproved = null;

    /**
     * Creates a new CustomerDataBuilder with default values.
     */
    public CustomerDataBuilder() {
        // Initialize with default documents from the JSON payload
        initializeDefaultDocuments();
    }

    /**
     * Initializes the default documents (IdentityDto and DriverLicenseDto) from the JSON payload.
     */
    private void initializeDefaultDocuments() {
        // Identity Document
        CreateOrUpdateCustomerRequestBean.Attachment identityAttachment =
                new CreateOrUpdateCustomerRequestBean.Attachment(
                        "https://ejar-jor-test.iyelo.com:9900/webapigw\\Temp\\Downloads\\087932aa-ed76-46ba-9637-e7a34b6cb5c2.png",
                        11649,
                        ".PNG"
                );

        CreateOrUpdateCustomerRequestBean.DocumentDto identityDocument =
                new CreateOrUpdateCustomerRequestBean.DocumentDto(
                        "IdentityDto",
                        "1",
                        "250",
                        "6370673284",
                        1,
                        "2020-08-26T00:00:00+03:00",
                        "2030-08-26T16:23:00+03:00",
                        "Identity",
                        "Saudi Arabia ",
                        identityAttachment,
                        null // licenseCategoryId not applicable for IdentityDto
                );
        documents.add(identityDocument);

        // Driver License Document
        CreateOrUpdateCustomerRequestBean.Attachment driverLicenseAttachment =
                new CreateOrUpdateCustomerRequestBean.Attachment(
                        "https://ejar-jor-test.iyelo.com:9900/webapigw\\Temp\\Downloads\\087932aa-ed76-46ba-9637-e7a34b6cb5c2.png",
                        11649,
                        ".PNG"
                );

        CreateOrUpdateCustomerRequestBean.DocumentDto driverLicenseDocument =
                new CreateOrUpdateCustomerRequestBean.DocumentDto(
                        "DriverLicenseDto",
                        "1",
                        "253",
                        "2668415730",
                        null, // copyNumber not in JSON for DriverLicense
                        "2020-08-26T00:00:00+03:00",
                        "2029-08-08T16:24:00+03:00",
                        "Driver License",
                        "Saudi Arabia",
                        driverLicenseAttachment,
                        "-1" // licenseCategoryId
                );
        documents.add(driverLicenseDocument);
    }

    // Builder methods for Full Name
    public CustomerDataBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public CustomerDataBuilder withSecondName(String secondName) {
        this.secondName = secondName;
        return this;
    }

    public CustomerDataBuilder withFamilyName(String familyName) {
        this.familyName = familyName;
        return this;
    }

    public CustomerDataBuilder withFullName(String first, String second, String family) {
        this.firstName = first;
        this.secondName = second;
        this.familyName = family;
        return this;
    }

    // Builder methods for Contact Information
    public CustomerDataBuilder withPrimaryPhone(String primaryPhone) {
        this.primaryPhone = primaryPhone;
        return this;
    }

    public CustomerDataBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public CustomerDataBuilder withContactInformation(String primaryPhone, String email) {
        this.primaryPhone = primaryPhone;
        this.email = email;
        return this;
    }

    // Builder methods for Basic Information
    public CustomerDataBuilder withNationalityId(String nationalityId) {
        this.nationalityId = nationalityId;
        return this;
    }

    public CustomerDataBuilder withGenderId(String genderId) {
        this.genderId = genderId;
        return this;
    }

    public CustomerDataBuilder withDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public CustomerDataBuilder withBasicInformation(String nationalityId, String genderId, String dateOfBirth) {
        this.nationalityId = nationalityId;
        this.genderId = genderId;
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    // Builder methods for Professional Information
    public CustomerDataBuilder withOrganizationId(String organizationId) {
        this.organizationId = organizationId;
        return this;
    }

    public CustomerDataBuilder withOrganizationName(String organizationName) {
        this.organizationName = organizationName;
        return this;
    }

    public CustomerDataBuilder withOccupationId(String occupationId) {
        this.occupationId = occupationId;
        return this;
    }

    public CustomerDataBuilder withProfessionalInformation(String organizationId, String organizationName, String occupationId) {
        this.organizationId = organizationId;
        this.organizationName = organizationName;
        this.occupationId = occupationId;
        return this;
    }

    // Builder methods for Address
    public CustomerDataBuilder withAddressCountryId(String countryId) {
        this.addressCountryId = countryId;
        return this;
    }

    public CustomerDataBuilder withAddressCityId(Integer cityId) {
        this.addressCityId = cityId;
        return this;
    }

    public CustomerDataBuilder withAddress(String countryId, Integer cityId) {
        this.addressCountryId = countryId;
        this.addressCityId = cityId;
        return this;
    }

    // Builder methods for Secondary Address
    public CustomerDataBuilder withSecondaryAddressCountryId(String countryId) {
        this.secondaryAddressCountryId = countryId;
        return this;
    }

    public CustomerDataBuilder withSecondaryAddressCityId(Integer cityId) {
        this.secondaryAddressCityId = cityId;
        return this;
    }

    public CustomerDataBuilder withSecondaryAddressDetails(String details) {
        this.secondaryAddressDetails = details;
        return this;
    }

    public CustomerDataBuilder withSecondaryAddress(String countryId, Integer cityId, String details) {
        this.secondaryAddressCountryId = countryId;
        this.secondaryAddressCityId = cityId;
        this.secondaryAddressDetails = details;
        return this;
    }

    // Builder methods for Emergency Contacts
    public CustomerDataBuilder withEmergencyContact(String name, String phone, String relationship) {
        CreateOrUpdateCustomerRequestBean.EmergencyContact contact =
                new CreateOrUpdateCustomerRequestBean.EmergencyContact(name, phone, relationship);
        this.emergencyContacts.add(contact);
        return this;
    }

    public CustomerDataBuilder withEmergencyContacts(List<CreateOrUpdateCustomerRequestBean.EmergencyContact> emergencyContacts) {
        this.emergencyContacts = emergencyContacts != null ? new ArrayList<>(emergencyContacts) : new ArrayList<>();
        return this;
    }

    public CustomerDataBuilder clearEmergencyContacts() {
        this.emergencyContacts.clear();
        return this;
    }

    // Builder methods for Personal Photo
    public CustomerDataBuilder withPersonalPhotoId(String id) {
        this.personalPhotoId = id;
        return this;
    }

    public CustomerDataBuilder withPersonalPhotoUrl(String url) {
        this.personalPhotoUrl = url;
        return this;
    }

    public CustomerDataBuilder withPersonalPhotoSize(Integer size) {
        this.personalPhotoSize = size;
        return this;
    }

    public CustomerDataBuilder withPersonalPhotoType(String type) {
        this.personalPhotoType = type;
        return this;
    }

    public CustomerDataBuilder withPersonalPhoto(String id, String url, Integer size, String type) {
        this.personalPhotoId = id;
        this.personalPhotoUrl = url;
        this.personalPhotoSize = size;
        this.personalPhotoType = type;
        return this;
    }

    // Builder methods for Documents
    public CustomerDataBuilder withDocument(CreateOrUpdateCustomerRequestBean.DocumentDto document) {
        this.documents.add(document);
        return this;
    }

    public CustomerDataBuilder withDocuments(List<CreateOrUpdateCustomerRequestBean.DocumentDto> documents) {
        this.documents = documents != null ? new ArrayList<>(documents) : new ArrayList<>();
        return this;
    }

    public CustomerDataBuilder clearDocuments() {
        this.documents.clear();
        return this;
    }

    // Builder methods for External Loyalties
    public CustomerDataBuilder withExternalLoyalty(Object loyalty) {
        this.externalLoyalties.add(loyalty);
        return this;
    }

    public CustomerDataBuilder withExternalLoyalties(List<Object> externalLoyalties) {
        this.externalLoyalties = externalLoyalties != null ? new ArrayList<>(externalLoyalties) : new ArrayList<>();
        return this;
    }

    public CustomerDataBuilder clearExternalLoyalties() {
        this.externalLoyalties.clear();
        return this;
    }

    // Builder method for Source ID
    public CustomerDataBuilder withSourceId(Integer sourceId) {
        this.sourceId = sourceId;
        return this;
    }

    /**
     * Builds and returns the CreateOrUpdateCustomerRequestBean with the configured values.
     *
     * @return CreateOrUpdateCustomerRequestBean instance
     */
    public CreateOrUpdateCustomerRequestBean build() {
        // Build Full Name
        CreateOrUpdateCustomerRequestBean.FullName fullName =
                new CreateOrUpdateCustomerRequestBean.FullName(firstName, secondName, familyName);

        // Build Contact Information
        CreateOrUpdateCustomerRequestBean.ContactInformation contactInformation =
                new CreateOrUpdateCustomerRequestBean.ContactInformation(primaryPhone, email);

        // Build Basic Information
        CreateOrUpdateCustomerRequestBean.BasicInformation basicInformation =
                new CreateOrUpdateCustomerRequestBean.BasicInformation(nationalityId, genderId, dateOfBirth);

        // Build Professional Information
        CreateOrUpdateCustomerRequestBean.ProfessionalInformation professionalInformation =
                new CreateOrUpdateCustomerRequestBean.ProfessionalInformation(
                        organizationId,
                        organizationName,
                        occupationId
                );

        // Build Customer Policy Verification
        CreateOrUpdateCustomerRequestBean.CustomerPolicyVerification customerPolicyVerification =
                new CreateOrUpdateCustomerRequestBean.CustomerPolicyVerification(
                        isTermsConditionsAndPrivacyPolicyApproved,
                        isMarketingMaterialsApproved,
                        isDataSharingPolicyApproved
                );

        // Build Address
        CreateOrUpdateCustomerRequestBean.Address address =
                new CreateOrUpdateCustomerRequestBean.Address(addressCountryId, addressCityId);

        // Build Secondary Address
        CreateOrUpdateCustomerRequestBean.SecondaryAddress secondaryAddress =
                new CreateOrUpdateCustomerRequestBean.SecondaryAddress(
                        secondaryAddressCountryId,
                        secondaryAddressCityId,
                        secondaryAddressDetails
                );

        // Build Personal Photo
        CreateOrUpdateCustomerRequestBean.PersonalPhoto personalPhoto =
                new CreateOrUpdateCustomerRequestBean.PersonalPhoto(
                        personalPhotoId,
                        personalPhotoUrl,
                        personalPhotoSize,
                        personalPhotoType
                );

        // Build Customer Data
        CreateOrUpdateCustomerRequestBean.CustomerData customerData =
                new CreateOrUpdateCustomerRequestBean.CustomerData(
                        id,
                        fullName,
                        contactInformation,
                        basicInformation,
                        professionalInformation,
                        customerPolicyVerification,
                        address,
                        secondaryAddress,
                        emergencyContacts,
                        personalPhoto,
                        documents,
                        externalLoyalties,
                        sourceId
                );

        // Build and return the final request
        return new CreateOrUpdateCustomerRequestBean(customerData);
    }

    /**
     * Creates a new CustomerDataBuilder with default values.
     *
     * @return new CustomerDataBuilder instance
     */
    public static CustomerDataBuilder create() {
        return new CustomerDataBuilder();
    }
}

