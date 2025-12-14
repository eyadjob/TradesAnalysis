package com.services;

import com.beans.customer.CreateOrUpdateCustomerRequestBean;
import com.builders.CustomerDataBuilder;
import com.pojo.CustomerCsvData;
import com.util.CustomerCsvImportUtil;
import com.util.DateUtil;
import com.util.StringUtil;
import com.util.XlsxWriterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.util.StringUtil.getValueOrEmpty;

@Service
public class CustomerOperationsService {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerCsvImportUtil customerCsvImportUtil;

    @Autowired
    private LookupsService lookupsService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private SettingsService settingsService;


    /**
     * Builds CreateOrUpdateCustomerRequestBean from CustomerCsvData.
     *
     * @return CreateOrUpdateCustomerRequestBean populated with CSV data
     */
    private CreateOrUpdateCustomerRequestBean buildRequestForCustomerCreation() {
        CustomerDataBuilder builder = CustomerDataBuilder.create();
        String firstName = StringUtil.generateRandomStringWithPrefix("Eyad Automation", 10);
        String secondName = StringUtil.generateRandomStringWithPrefix("Eyad Automation", 10);
        String thirdName = StringUtil.generateRandomStringWithPrefix("Eyad Automation", 10);
        String phoneNumber = "966-51" +StringUtil.generateRandomNumber(7);

        // Map Full Name
        builder.withFullName(
                firstName,
                secondName,
                thirdName
        );

        // Map Contact Information
        builder.withContactInformation(
                phoneNumber,
               firstName+"@iyelo.com"
        );

        // Map Basic Information
        builder.withBasicInformation(
                String. valueOf(getNationalityIdByName(csvData.nationality())),
                getComboboxItemsValueByDisplayText(csvData.gender(),6),
                DateUtil.formatDateToRenteyFormat(csvData.birthDate())
        );

        // Map Address (using DocumentIssueCountry as default, or "1" if not available)
        String countryId = String.valueOf(getNationalityIdByName(csvData.documentIssueCountry()));
        builder.withAddress(countryId, -1); // -1 for cityId as default

        // Clear default documents and build from CSV data
        builder.clearDocuments();

        // Add Identity Document if DocumentNumber is provided
        if (isNotEmpty(csvData.documentNumber())) {
            CreateOrUpdateCustomerRequestBean.DocumentDto identityDocument = buildIdentityDocument(csvData);
            builder.withDocument(identityDocument);
        }

        // Add Driver License Document if licenseNo is provided
        if (isNotEmpty(csvData.licenseNo())) {
            CreateOrUpdateCustomerRequestBean.DocumentDto driverLicenseDocument = buildDriverLicenseDocument(csvData);
            builder.withDocument(driverLicenseDocument);
        }

        return builder.build();
    }

}
