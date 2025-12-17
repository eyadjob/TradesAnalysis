package com.services;

import com.beans.customer.CreateOrUpdateCustomerRequestBean;
import com.beans.customer.CreateOrUpdateCustomerResponseBean;
import com.builders.CustomerDataBuilder;
import com.enums.CustomerDocumentType;
import com.util.DateUtil;
import com.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerOperationsService {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private LookupsService lookupsService;

    @Autowired
    private CountryService countryService;

    @Autowired
    VehicleService vehicleService;

    @Autowired
    @org.springframework.beans.factory.annotation.Qualifier("settingsApiBaseUrl")
    private String settingsApiBaseUrl;


    public CreateOrUpdateCustomerResponseBean createCustomerWithRandomData(String countryName) {
        CreateOrUpdateCustomerRequestBean createOrUpdateCustomerRequestBean = buildCustomerCreationRequestBean(countryName);
        return  customerService.createOrUpdateCustomer(createOrUpdateCustomerRequestBean);
    }


    /**
     * Builds CreateOrUpdateCustomerRequestBean from CustomerCsvData.
     *
     */
    private CreateOrUpdateCustomerRequestBean buildCustomerCreationRequestBean(String countryName) {
        CustomerDataBuilder builder = CustomerDataBuilder.create(settingsApiBaseUrl);
        String countryId = countryService.getOperationalCountryIdFromName(countryName);
        String identityDocumentTypeId = lookupsService.getComboboxItemsValueByDisplayText(CustomerDocumentType.IDENTITY.getDisplayText(),17);
        String driverLicenseDocumentTypeId = lookupsService.getComboboxItemsValueByDisplayText(CustomerDocumentType.DRIVER_LICENSE.getDisplayText(),17);
        String firstName = StringUtil.generateRandomStringWithPrefix("Eyad Automation", 10);
        String secondName = StringUtil.generateRandomStringWithPrefix("Eyad Automation", 10);
        String thirdName = StringUtil.generateRandomStringWithPrefix("Eyad Automation", 10);
        String phoneNumber = "966-51" + StringUtil.generateRandomNumber(7);
        String documentNumber = StringUtil.generateRandomNumber(10);
        String imagePath =  vehicleService.uploadSignatureImage();

        // Map Full Name
        builder.withFullName(
                firstName,
                secondName,
                thirdName
        );
        
        return buildCustomerCreationRequestBeanInternal(builder, countryName, countryId, identityDocumentTypeId, driverLicenseDocumentTypeId, firstName, phoneNumber, documentNumber, imagePath);
    }

    /**
     * Builds CreateOrUpdateCustomerRequestBean with provided names.
     * Overloaded method that accepts firstName, secondName, and thirdName as parameters.
     *
     * @param countryName The name of the country for the customer.
     * @param firstName The first name of the customer.
     * @param secondName The second name of the customer.
     * @param thirdName The third name (family name) of the customer.
     * @return CreateOrUpdateCustomerRequestBean instance.
     */
    private CreateOrUpdateCustomerRequestBean buildCustomerCreationRequestBean(String countryName, String firstName, String secondName, String thirdName) {
        CustomerDataBuilder customerDataBuilder = CustomerDataBuilder.create(settingsApiBaseUrl);
        String countryId = countryService.getOperationalCountryIdFromName(countryName);
        String identityDocumentTypeId = lookupsService.getComboboxItemsValueByDisplayText(CustomerDocumentType.IDENTITY.getDisplayText(),17);
        String driverLicenseDocumentTypeId = lookupsService.getComboboxItemsValueByDisplayText(CustomerDocumentType.DRIVER_LICENSE.getDisplayText(),17);
        String phoneNumber = "966-51" + StringUtil.generateRandomNumber(7);
        String documentNumber = StringUtil.generateRandomNumber(10);
        String imagePath =  vehicleService.uploadSignatureImage();

        // Map Full Name using provided names
        customerDataBuilder.withFullName(
                firstName,
                secondName,
                thirdName
        );
        
        return buildCustomerCreationRequestBeanInternal(customerDataBuilder, countryName, countryId, identityDocumentTypeId, driverLicenseDocumentTypeId, firstName, phoneNumber, documentNumber, imagePath);
    }

    /**
     * Builds CreateOrUpdateCustomerRequestBean with provided names and phone number.
     * Overloaded method that accepts firstName, secondName, thirdName, and phoneNumber as parameters.
     *
     * @param countryName The name of the country for the customer.
     * @param firstName The first name of the customer.
     * @param secondName The second name of the customer.
     * @param thirdName The third name (family name) of the customer.
     * @param phoneNumber The phone number of the customer.
     * @return CreateOrUpdateCustomerRequestBean instance.
     */
    private CreateOrUpdateCustomerRequestBean buildCustomerCreationRequestBean(String countryName, String firstName, String secondName, String thirdName, String phoneNumber) {
        CustomerDataBuilder customerDataBuilder = CustomerDataBuilder.create(settingsApiBaseUrl);
        String countryId = countryService.getOperationalCountryIdFromName(countryName);
        String identityDocumentTypeId = lookupsService.getComboboxItemsValueByDisplayText(CustomerDocumentType.IDENTITY.getDisplayText(),17);
        String driverLicenseDocumentTypeId = lookupsService.getComboboxItemsValueByDisplayText(CustomerDocumentType.DRIVER_LICENSE.getDisplayText(),17);
        String documentNumber = StringUtil.generateRandomNumber(10);
        String imagePath =  vehicleService.uploadSignatureImage();

        // Map Full Name using provided names
        customerDataBuilder.withFullName(
                firstName,
                secondName,
                thirdName
        );
        
        return buildCustomerCreationRequestBeanInternal(customerDataBuilder, countryName, countryId, identityDocumentTypeId, driverLicenseDocumentTypeId, firstName, phoneNumber, documentNumber, imagePath);
    }

    /**
     * Internal helper method to build the customer request bean with common logic.
     */
    private CreateOrUpdateCustomerRequestBean buildCustomerCreationRequestBeanInternal(
            CustomerDataBuilder builder,
            String countryName,
            String countryId,
            String identityDocumentTypeId,
            String driverLicenseDocumentTypeId,
            String firstName,
            String phoneNumber,
            String documentNumber,
            String imagePath) {

        builder.withContactInformation(
                phoneNumber,
                firstName.replace(" ","")+ "@iyelo.com"
        );

        builder.withBasicInformation(
                countryId,
                lookupsService.getComboboxItemsValueByDisplayText("Male", 6),
                DateUtil.formatDateToRenteyFormat("1989-01-15")
        );

        builder.withAddress(countryId, -1); // -1 for cityId as default
        builder.clearDocuments();

       CreateOrUpdateCustomerRequestBean.Attachment documentAttachment = builder.getDocumentAttachment(imagePath);
        CreateOrUpdateCustomerRequestBean.DocumentDto identityDocument = new CreateOrUpdateCustomerRequestBean.DocumentDto(
                "IdentityDto",
                countryId,
                identityDocumentTypeId,
                documentNumber,
                1, // copyNumber
                DateUtil.formatDateToRenteyFormat("1989-01-15"), // issueDate
                DateUtil.formatDateToRenteyFormat("2035-01-20"), // expiryDate
                "Identity", // typeName
                countryName,
                documentAttachment,// issueCountry
               null // licenseCategoryId (not applicable for Identity)
        );
        builder.withDocument(identityDocument);


                CreateOrUpdateCustomerRequestBean.DocumentDto driverLicenseDocument  = new CreateOrUpdateCustomerRequestBean.DocumentDto(
                "DriverLicenseDto",
                countryId,
                driverLicenseDocumentTypeId,
                documentNumber,
                1, // copyNumber
                DateUtil.formatDateToRenteyFormat("1989-01-15"), // issueDate
                DateUtil.formatDateToRenteyFormat("2035-01-20"), // expiryDate
                "Driver License", // typeName
                countryName, // issueCountry
                documentAttachment,
                null// licenseCategoryId (not applicable for Identity)
        );
        builder.withDocument(driverLicenseDocument);

        return builder.build();
    }

}
