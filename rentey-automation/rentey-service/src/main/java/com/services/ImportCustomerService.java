package com.services;

import com.beans.CreateOrUpdateCustomerRequestBean;
import com.beans.CreateOrUpdateCustomerResponseBean;
import com.beans.GetAllItemsComboboxItemsResponseBean;
import com.beans.GetOperationalCountriesResponseBean;
import com.builders.CustomerDataBuilder;
import com.pojo.CustomerCsvData;
import com.util.CustomerCsvImportUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImportCustomerService {

    private static final Logger logger = LoggerFactory.getLogger(ImportCustomerService.class);

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerCsvImportUtil customerCsvImportUtil;

    @Autowired
    private LookupsService lookupsService;

    @Autowired
    private SettingsService settingsService;


    private GetAllItemsComboboxItemsResponseBean genderLookupValues;
    private GetOperationalCountriesResponseBean countriesResponseBean;
    private GetAllItemsComboboxItemsResponseBean customerDocumentTypes;

    public CreateOrUpdateCustomerResponseBean importCustomerRecordsToSystemFromCsvFile(CreateOrUpdateCustomerRequestBean request) {
        List<CustomerCsvData> customerCsvDataList = customerCsvImportUtil.importCsvFiles();
        List<CreateOrUpdateCustomerResponseBean> responses = new ArrayList<>();

        logger.info("Starting import of {} customer records from CSV", customerCsvDataList.size());

        for (CustomerCsvData customerCsvData : customerCsvDataList) {
            try {
                CreateOrUpdateCustomerRequestBean createOrUpdateCustomerRequestBean = buildRequestFromCsvData(customerCsvData);
                CreateOrUpdateCustomerResponseBean response = customerService.createOrUpdateCustomer(createOrUpdateCustomerRequestBean);
                responses.add(response);
                logger.info("Successfully imported customer: {} {} {}",
                        customerCsvData.firstName(), customerCsvData.secondName(), customerCsvData.familyName());
            } catch (Exception e) {
                logger.error("Failed to import customer: {} {} {} - Error: {}",
                        customerCsvData.firstName(), customerCsvData.secondName(), customerCsvData.familyName(),
                        e.getMessage(), e);
            }
        }

        logger.info("Completed import process. Successfully imported {}/{} customers",
                responses.size(), customerCsvDataList.size());

        // Return the last response or null if no records were processed
        return responses.isEmpty() ? null : responses.get(responses.size() - 1);
    }

    /**
     * Builds CreateOrUpdateCustomerRequestBean from CustomerCsvData.
     *
     * @param csvData The CSV data to map
     * @return CreateOrUpdateCustomerRequestBean populated with CSV data
     */
    private CreateOrUpdateCustomerRequestBean buildRequestFromCsvData(CustomerCsvData csvData) {
        CustomerDataBuilder builder = CustomerDataBuilder.create();

        // Map Full Name
        builder.withFullName(
                getValueOrEmpty(csvData.firstName()),
                getValueOrEmpty(csvData.secondName()),
                getValueOrEmpty(csvData.familyName())
        );

        // Map Contact Information
        builder.withContactInformation(
                getValueOrEmpty(csvData.primaryPhone()),
                getValueOrEmpty("")
        );

        // Map Basic Information
        builder.withBasicInformation(
                getValueOrEmpty(csvData.nationality()),
                getComboboxItemsValueFromDisplayText(csvData.gender(),6),
                formatDateOfBirth(csvData.birthDate())
        );

        // Map Address (using DocumentIssueCountry as default, or "1" if not available)
        String countryId = getOperationalCountryIdFromName(csvData.documentIssueCountry());
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

    /**
     * Builds an Identity Document from CSV data.
     */
    private CreateOrUpdateCustomerRequestBean.DocumentDto buildIdentityDocument(CustomerCsvData csvData) {
        String issueCountryId = getOperationalCountryIdFromName(csvData.documentIssueCountry());
        String documentTypeId = getComboboxItemsValueFromDisplayText(csvData.documentType(),17);

        CreateOrUpdateCustomerRequestBean.Attachment attachment = new CreateOrUpdateCustomerRequestBean.Attachment(
                "", // URL - empty for CSV import
                0,  // Size
                ""  // Type
        );

        return new CreateOrUpdateCustomerRequestBean.DocumentDto(
                "IdentityDto",
                issueCountryId,
                documentTypeId,
                getValueOrEmpty(csvData.documentNumber()),
                1, // Default copyNumber
                formatDate(csvData.birthDate()), // Use birthDate as issueDate if available
                formatDate(csvData.documentExpireDate()),
                "Identity",
                getValueOrEmpty(csvData.documentIssueCountry()),
                attachment,
                null // licenseCategoryId not applicable for IdentityDto
        );
    }

    /**
     * Builds a Driver License Document from CSV data.
     */
    private CreateOrUpdateCustomerRequestBean.DocumentDto buildDriverLicenseDocument(CustomerCsvData csvData) {
        String issueCountryId = getValueOrEmpty(csvData.licenseIssueCountry());
        if (issueCountryId.isEmpty()) {
            issueCountryId = getValueOrEmpty(csvData.documentIssueCountry());
            if (issueCountryId.isEmpty()) {
                issueCountryId = "1"; // Default country ID
            }
        }

        CreateOrUpdateCustomerRequestBean.Attachment attachment = new CreateOrUpdateCustomerRequestBean.Attachment(
                "", // URL - empty for CSV import
                0,  // Size
                ""  // Type
        );

        return new CreateOrUpdateCustomerRequestBean.DocumentDto(
                "DriverLicenseDto",
                issueCountryId,
                "253", // Default document type ID for Driver License
                getValueOrEmpty(csvData.licenseNo()),
                null, // copyNumber not applicable for DriverLicense
                formatDate(csvData.birthDate()), // Use birthDate as issueDate if available
                formatDate(csvData.licenseExpiryDate()),
                "Driver License",
                getValueOrEmpty(csvData.licenseIssueCountry()),
                attachment,
                "-1" // Default licenseCategoryId
        );
    }

    /**
     * Gets value or returns empty string if null.
     */
    private String getValueOrEmpty(String value) {
        return value != null && !value.trim().isEmpty() ? value.trim() : "";
    }

    /**
     * Checks if a string is not empty.
     */
    private boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Generates an email address from first name and family name.
     */
    private String generateEmailFromName(String firstName, String familyName) {
        String first = getValueOrEmpty(firstName).toLowerCase().replaceAll("[^a-z0-9]", "");
        String family = getValueOrEmpty(familyName).toLowerCase().replaceAll("[^a-z0-9]", "");
        if (first.isEmpty() && family.isEmpty()) {
            return "customer@iyelo.com";
        }
        return (first + family + "@iyelo.com").toLowerCase();
    }

    /**
     * Formats date of birth to ISO format.
     * If the date is already in ISO format, returns as is.
     * Otherwise, attempts to parse common date formats.
     */
    private String formatDateOfBirth(String dateStr) {
        if (!isNotEmpty(dateStr)) {
            return "1999-08-26T16:14:05+03:00"; // Default date
        }
        // If already in ISO format, return as is
        if (dateStr.contains("T") || dateStr.contains("+") || dateStr.contains("Z")) {
            return dateStr;
        }
        // Try to format common date formats (YYYY-MM-DD, DD/MM/YYYY, etc.)
        // For now, append default time if just date is provided
        if (dateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return dateStr + "T00:00:00+03:00";
        }
        return dateStr; // Return as is if format is unknown
    }

    /**
     * Formats a date string. Returns empty string if null/empty, otherwise returns as is.
     */
    private String formatDate(String dateStr) {
        if (!isNotEmpty(dateStr)) {
            return "";
        }
        // If already in ISO format, return as is
        if (dateStr.contains("T") || dateStr.contains("+") || dateStr.contains("Z")) {
            return dateStr;
        }
        // Try to format common date formats
        if (dateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return dateStr + "T00:00:00+03:00";
        }
        return dateStr;
    }

    private String getComboboxItemsValueFromDisplayText(String csvDataDisplayText, int typeId) {
        if (this.genderLookupValues == null) {
            this.genderLookupValues = lookupsService.getAllItemsComboboxItems(typeId, false, false);
        }
        for (GetAllItemsComboboxItemsResponseBean.ComboboxItem genderLookupValue : this.genderLookupValues.result().items()) {
            if (csvDataDisplayText.trim().equals(genderLookupValue.displayText())) {
                return genderLookupValue.value();
            }
        }
        return null;
    }

    private String getOperationalCountryIdFromName(String documentIssueCountry) {
        if (this.countriesResponseBean == null) {
            this.countriesResponseBean = settingsService.getOperationalCountries();
        } else {
            List<Integer> countryIds = new ArrayList<>();
            for (GetOperationalCountriesResponseBean.OperationalCountry country : countriesResponseBean.result()) {
                if (country.name().equals(documentIssueCountry)) {
                    return String.valueOf(country.id());
                }
            }
        }
        return "-1";
    }
}

