package com.services;

import com.annotation.LogExecutionTime;
import com.annotation.LogRequestAndResponseOnDesk;
import com.beans.customer.CreateOrUpdateCustomerRequestBean;
import com.beans.customer.CreateOrUpdateCustomerResponseBean;
import com.beans.general.GetAllItemsComboboxItemsResponseBean;
import com.beans.setting.GetOperationalCountriesResponseBean;
import com.builders.CustomerDataBuilder;
import com.enums.LookupType;
import com.pojo.CustomerCsvData;
import com.util.CustomerCsvImportUtil;
import com.util.DateUtil;
import com.util.XlsxWriterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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

    @Autowired
    private XlsxWriterUtil xlsxWriterUtil;

    @Value("${csv.export.directory}")
    private String exportDirectory;

    private GetAllItemsComboboxItemsResponseBean lookupTypes;
    private GetAllItemsComboboxItemsResponseBean genderLookupValues;
    private GetOperationalCountriesResponseBean countriesResponseBean;
    private GetAllItemsComboboxItemsResponseBean customerDocumentTypes;
    private GetAllItemsComboboxItemsResponseBean nationalities;

    private static final Map<String, String> countryIso = loadCountryIsoMap();

    /**
     * Loads the country ISO code to country name mapping from the properties file.
     *
     * @return Map of country ISO codes to country names
     */
    private static Map<String, String> loadCountryIsoMap() {
        Map<String, String> map = new HashMap<>();
        Properties properties = new Properties();
        
        try (InputStream inputStream = ImportCustomerService.class.getClassLoader()
                .getResourceAsStream("country-iso.properties")) {
            if (inputStream == null) {
                logger.error("country-iso.properties file not found in classpath");
                return map;
            }
            
            properties.load(inputStream);
            
            for (String key : properties.stringPropertyNames()) {
                map.put(key, properties.getProperty(key));
            }
            
            logger.info("Loaded {} country ISO mappings from properties file", map.size());
        } catch (IOException e) {
            logger.error("Error loading country-iso.properties file", e);
        }
        
        return map;
    }

    public ImportCustomerService() {

    }
    @LogRequestAndResponseOnDesk
    @LogExecutionTime
    public CreateOrUpdateCustomerResponseBean importCustomerRecordsToSystemFromCsvFile() {
        List<CustomerCsvData> customerCsvDataList = customerCsvImportUtil.getCsvFiles();
        List<CreateOrUpdateCustomerResponseBean> responses = new ArrayList<>();
        List<String> responseCodes = new ArrayList<>();
        List<String> responseMessages = new ArrayList<>();

        logger.info("Starting import of {} customer records from CSV", customerCsvDataList.size());

        for (CustomerCsvData customerCsvData : customerCsvDataList) {
            try {
                CreateOrUpdateCustomerRequestBean createOrUpdateCustomerRequestBean = buildRequestFromCsvData(customerCsvData);
                CreateOrUpdateCustomerResponseBean response = customerService.createOrUpdateCustomer(createOrUpdateCustomerRequestBean);
                responses.add(response);
                
                // Extract response code and message
                String responseCode = extractResponseCode(response);
                String responseMessage = extractResponseMessage(response);
                responseCodes.add(responseCode);
                responseMessages.add(responseMessage);
                
                logger.info("Successfully imported customer: {} {} {} - Code: {}, Message: {}",
                        customerCsvData.firstName(), customerCsvData.secondName(), customerCsvData.familyName(),
                        responseCode, responseMessage);
            } catch (org.springframework.web.reactive.function.client.WebClientResponseException e) {
                // Handle WebClient response exceptions
                String errorCode = String.valueOf(e.getStatusCode().value());
                String errorMessage = extractErrorMessageFromException(e);
                responseCodes.add(errorCode);
                responseMessages.add(errorMessage);
                responses.add(null); // Add null to maintain list alignment
                
                logger.error("Failed to import customer: {} {} {} - Status: {}, Error: {}",
                        customerCsvData.firstName(), customerCsvData.secondName(), customerCsvData.familyName(),
                        errorCode, errorMessage, e);
            } catch (Exception e) {
                // Handle other exceptions
                String errorCode = "ERROR";
                String errorMessage = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
                responseCodes.add(errorCode);
                responseMessages.add(errorMessage);
                responses.add(null); // Add null to maintain list alignment
                
                logger.error("Failed to import customer: {} {} {} - Error: {}",
                        customerCsvData.firstName(), customerCsvData.secondName(), customerCsvData.familyName(),
                        errorMessage, e);
            }
        }

        logger.info("Completed import process. Successfully imported {}/{} customers",
                responses.size(), customerCsvDataList.size());

        // Write results to XLSX file
        try {
            // Resolve export directory path relative to project root
            String exportDirPath = System.getProperty("user.dir") + "\\" + exportDirectory;
            String xlsxFilePath = xlsxWriterUtil.writeImportResultsToXlsx(
                    customerCsvDataList,
                    responseCodes,
                    responseMessages,
                    exportDirPath);
            if (xlsxFilePath != null) {
                logger.info("Import results written to XLSX file: {}", xlsxFilePath);
            } else {
                logger.warn("Failed to write import results to XLSX file");
            }
        } catch (Exception e) {
            logger.error("Error writing import results to XLSX file", e);
        }

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

    /**
     * Builds an Identity Document from CSV data.
     */
    private CreateOrUpdateCustomerRequestBean.DocumentDto buildIdentityDocument(CustomerCsvData csvData) {
        String issueCountryId = String.valueOf(getNationalityIdByName(csvData.documentIssueCountry()));
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
                DateUtil.formatDateToRenteyFormat(csvData.birthDate()), // Use birthDate as issueDate if available
                DateUtil.formatDateToRenteyFormat(csvData.documentExpireDate()),
                "Identity",
                countryIso.get(csvData.documentIssueCountry()),
                attachment,
                null
        );
    }

    /**
     * Builds a Driver License Document from CSV data.
     */
    private CreateOrUpdateCustomerRequestBean.DocumentDto buildDriverLicenseDocument(CustomerCsvData csvData) {
        String issueCountryId = String.valueOf(getNationalityIdByName(csvData.licenseIssueCountry()));
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
                DateUtil.formatDateToRenteyFormat(csvData.birthDate()), // Use birthDate as issueDate if available
                DateUtil.formatDateToRenteyFormat(csvData.licenseExpiryDate()),
                "Driver License",
                countryIso.get(csvData.licenseIssueCountry()),
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
     * Extracts response code from CreateOrUpdateCustomerResponseBean.
     */
    private String extractResponseCode(CreateOrUpdateCustomerResponseBean response) {
        if (response == null) {
            return "NULL";
        }
        if (Boolean.TRUE.equals(response.success())) {
            return "200"; // Success
        } else {
            // Try to extract error code from error field
            if (response.error() != null) {
                // Error might be a JSON string, try to parse it
                try {
                    com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                    com.fasterxml.jackson.databind.JsonNode errorNode = mapper.readTree(response.error());
                    if (errorNode.has("code")) {
                        return String.valueOf(errorNode.get("code").asInt());
                    }
                } catch (Exception e) {
                    // If parsing fails, return generic error code
                }
            }
            return "ERROR";
        }
    }

    /**
     * Extracts response message from CreateOrUpdateCustomerResponseBean.
     */
    private String extractResponseMessage(CreateOrUpdateCustomerResponseBean response) {
        if (response == null) {
            return "No response received";
        }
        if (Boolean.TRUE.equals(response.success())) {
            return "Customer imported successfully";
        } else {
            // Try to extract error message from error field
            if (response.error() != null) {
                try {
                    com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                    com.fasterxml.jackson.databind.JsonNode errorNode = mapper.readTree(response.error());
                    if (errorNode.has("message")) {
                        return errorNode.get("message").asText();
                    }
                    // If no message field, return the error string as is
                    return response.error();
                } catch (Exception e) {
                    // If parsing fails, return the error string as is
                    return response.error() != null ? response.error() : "Unknown error";
                }
            }
            return "Unknown error";
        }
    }

    /**
     * Extracts error message from WebClientResponseException.
     */
    private String extractErrorMessageFromException(org.springframework.web.reactive.function.client.WebClientResponseException e) {
        try {
            String responseBody = e.getResponseBodyAsString();
            if (responseBody != null && !responseBody.trim().isEmpty()) {
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                com.fasterxml.jackson.databind.JsonNode rootNode = mapper.readTree(responseBody);
                
                // Try to extract error message from ABP response structure
                if (rootNode.has("error")) {
                    com.fasterxml.jackson.databind.JsonNode errorNode = rootNode.get("error");
                    if (errorNode.has("message")) {
                        return errorNode.get("message").asText();
                    }
                    if (errorNode.isTextual()) {
                        return errorNode.asText();
                    }
                }
                
                // If no error field, return the full response body (truncated if too long)
                return responseBody.length() > 500 ? responseBody.substring(0, 500) + "..." : responseBody;
            }
        } catch (Exception ex) {
            logger.warn("Error parsing exception response body: {}", ex.getMessage());
        }
        
        // Fallback to exception message
        return e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
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


    public int getNationalityIdByName(String nationalityIsoCode) {
        if ( this.nationalities==null) {
            this.nationalities = countryService.getCountriesForCombobox(false,false);
        }
        for (GetAllItemsComboboxItemsResponseBean.ComboboxItem nationality : nationalities.result().items()) {
            if ( countryIso.get(nationalityIsoCode).equals(nationality.displayText()))
                return Integer.parseInt(nationality.value());
            }
        return -1;
    }
}

