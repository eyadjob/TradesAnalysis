package com.services;

import com.annotation.LogExecutionTime;
import com.annotation.LogRequestAndResponseOnDesk;
import com.beans.CreateOrUpdateCustomerRequestBean;
import com.beans.CreateOrUpdateCustomerResponseBean;
import com.beans.GetAllItemsComboboxItemsResponseBean;
import com.beans.GetOperationalCountriesResponseBean;
import com.builders.CustomerDataBuilder;
import com.enums.LookupType;
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
    private CountryService countryService;

    @Autowired
    private SettingsService settingsService;

    private GetAllItemsComboboxItemsResponseBean lookupTypes;
    private GetAllItemsComboboxItemsResponseBean genderLookupValues;
    private GetOperationalCountriesResponseBean countriesResponseBean;
    private GetAllItemsComboboxItemsResponseBean customerDocumentTypes;
    private GetAllItemsComboboxItemsResponseBean nationalities;


    public ImportCustomerService() {

    }
    @LogRequestAndResponseOnDesk
    @LogExecutionTime
    public CreateOrUpdateCustomerResponseBean importCustomerRecordsToSystemFromCsvFile() {
        List<CustomerCsvData> customerCsvDataList = customerCsvImportUtil.getCsvFiles();
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
                String. valueOf(getNationalityIdByName(csvData.nationality())),
                getComboboxItemsValueByDisplayText(csvData.gender(),6),
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
        String documentTypeId = getComboboxItemsValueByDisplayText(csvData.documentType(),17);

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
                getOperationalCountryIdFromName(csvData.documentIssueCountry()),
                attachment,
                null
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
                getComboboxItemsValueByDisplayText("Driver License",getLookupTypeIdByName(LookupType.DRIVER_LICENSE_CATEGORY.getDisplayText())), // Default document type ID for Driver License
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
     * Formats date of birth to ISO format: YYYY-MM-DDTHH:mm:ss+03:00
     * If the date already includes time, returns it with timezone +03:00.
     * If only date is provided (without time), appends T00:00:00+03:00.
     * 
     * @param dateStr The date string to format
     * @return Formatted date string in ISO format with timezone +03:00
     */
    private String formatDateOfBirth(String dateStr) {
        if (!isNotEmpty(dateStr)) {
            return "1999-08-26T00:00:00+03:00"; // Default date with time
        }
        
        String trimmed = dateStr.trim();
        
        // If already in ISO format with time (contains T)
        if (trimmed.contains("T")) {
            // Extract date and time part (before timezone if exists)
            String dateTimePart;
            if (trimmed.contains("+")) {
                dateTimePart = trimmed.substring(0, trimmed.indexOf("+"));
            } else if (trimmed.endsWith("Z")) {
                dateTimePart = trimmed.substring(0, trimmed.length() - 1);
            } else {
                dateTimePart = trimmed;
            }
            
            // Ensure format is YYYY-MM-DDTHH:mm:ss, then add timezone
            if (dateTimePart.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}")) {
                return dateTimePart + "+03:00";
            }
            // If format is close but missing seconds, add them
            if (dateTimePart.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}")) {
                return dateTimePart + ":00+03:00";
            }
            // Return as is if format is already correct
            return trimmed;
        }
        
        // If only date is provided (YYYY-MM-DD format)
        if (trimmed.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return trimmed + "T00:00:00+03:00";
        }
        
        // YYYY/MM/DD format (e.g., 1989/02/28)
        if (trimmed.matches("\\d{4}/\\d{1,2}/\\d{1,2}")) {
            String[] parts = trimmed.split("/");
            if (parts.length == 3) {
                String year = parts[0];
                String month = String.format("%02d", Integer.parseInt(parts[1]));
                String day = String.format("%02d", Integer.parseInt(parts[2]));
                return year + "-" + month + "-" + day + "T00:00:00+03:00";
            }
        }
        
        // Try other common date formats
        // DD/MM/YYYY format
        if (trimmed.matches("\\d{1,2}/\\d{1,2}/\\d{4}")) {
            String[] parts = trimmed.split("/");
            if (parts.length == 3) {
                String year = parts[2];
                String month = String.format("%02d", Integer.parseInt(parts[1]));
                String day = String.format("%02d", Integer.parseInt(parts[0]));
                return year + "-" + month + "-" + day + "T00:00:00+03:00";
            }
        }
        
        // If format is unknown, log warning and return default
        logger.warn("Unknown date format: {}, using default", dateStr);
        return "1999-08-26T00:00:00+03:00";
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

    private String getComboboxItemsValueByDisplayText(String csvDataDisplayText, int typeId) {
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

    public int getLookupTypeIdByName(String lookupTypeName) {
        if ( this.lookupTypes==null) {
            this.lookupTypes = lookupsService.getTypesComboboxItems();
        }
        for (GetAllItemsComboboxItemsResponseBean.ComboboxItem lookupType : lookupTypes.result().items()) {
            if (lookupType.equals(lookupTypeName.trim())){
                return Integer.parseInt(lookupType.value());
            }
        }
        return -1;
    }


    public int getNationalityIdByName(String nationalityByName) {
        if ( this.nationalities==null) {
            this.nationalities = countryService.getCountriesForCombobox(false,false);
        }
        for (GetAllItemsComboboxItemsResponseBean.ComboboxItem nationality : nationalities.result().items()) {
            if (nationality.equals(nationalityByName.trim())){
                return Integer.parseInt(nationality.value());
            }
        }
        return -1;
    }
}

