package com.services;

import com.annotation.LogExecutionTime;
import com.annotation.LogRequestAndResponseOnDesk;
import com.beans.customer.CreateOrUpdateCustomerRequestBean;
import com.beans.customer.CreateOrUpdateCustomerResponseBean;
import com.builders.CustomerDataBuilder;
import com.enums.LookupTypes;
import com.pojo.CustomerCsvData;
import com.util.DateUtil;
import com.util.ObjectMapperUtil;
import com.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    @Qualifier("settingsWebClient")
    private WebClient webClient;

    @Autowired
    private ObjectMapperUtil objectMapperUtil;

    @Autowired
    @Qualifier("apiBasePath")
    private String apiBasePath;

    @Autowired
    private LookupsService lookupsService;

    @Autowired
    private CountryService countryService;

    @Autowired
    @org.springframework.beans.factory.annotation.Qualifier("settingsApiBaseUrl")
    private String settingsApiBaseUrl;


    /**
     * Create or update a customer.
     * This method automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     * On error (e.g., 500), returns a response with error message and status code instead of throwing.
     *
     * @param request The request containing customer information.
     * @return The response containing the created or updated customer data, or error response on failure.
     */
    @LogRequestAndResponseOnDesk
    @LogExecutionTime
    public CreateOrUpdateCustomerResponseBean createOrUpdateCustomer(CreateOrUpdateCustomerRequestBean request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null.");
        }

        // Serialize request body for error logging
        final String requestJson = objectMapperUtil.toJsonStringWithLogging(request);


        // Authorization header and all other headers from RenteyConfiguration are automatically included
        return webClient.post().uri(apiBasePath + "/Customer/CreateOrUpdateCustomer").bodyValue(request).retrieve().bodyToMono(CreateOrUpdateCustomerResponseBean.class).doOnError(error -> {
            logger.error("=== Error calling external API ===");
            logger.error("Error message: {}", error.getMessage());
            logger.error("Request Body: {}", requestJson);
            logger.error("Note: Request headers are logged by WebClientLoggingFilter above");
            logger.error("===================================");
        }).block();
    }


    /**
     * Builds a Driver License Document from CSV data.
     *
     * @param csvData The CSV data containing driver license information
     * @return DocumentDto for driver license document
     */
    public CreateOrUpdateCustomerRequestBean.DocumentDto buildDriverLicenseDocument(CustomerCsvData csvData) {
        String issueCountryId = String.valueOf(lookupsService.getNationalityIdByName(csvData.licenseIssueCountry()));
        CreateOrUpdateCustomerRequestBean.Attachment attachment = new CreateOrUpdateCustomerRequestBean.Attachment(
                "", // URL - empty for CSV import
                0,  // Size
                ""  // Type
        );

        return new CreateOrUpdateCustomerRequestBean.DocumentDto(
                "DriverLicenseDto",
                issueCountryId,
                lookupsService.getComboboxItemsValueByDisplayText("Driver License", lookupsService.getLookupTypeIdByName(LookupTypes.DRIVER_LICENSE_CATEGORY.getDisplayText())), // Default document type ID for Driver License
                StringUtil.getValueOrEmpty(csvData.licenseNo()),
                null, // copyNumber not applicable for DriverLicense
                DateUtil.formatDateToRenteyFormat(csvData.birthDate()), // Use birthDate as issueDate if available
                DateUtil.formatDateToRenteyFormat(csvData.licenseExpiryDate()),
                "Driver License",
                lookupsService.getCountryNameByIsoCode(csvData.licenseIssueCountry()),
                attachment,
                "-1" // Default licenseCategoryId
        );
    }


    /**
     * Builds an Identity Document from CSV data.
     *
     * @param csvData The CSV data containing document information
     * @return DocumentDto for identity document
     */
    public CreateOrUpdateCustomerRequestBean.DocumentDto buildIdentityDocument(CustomerCsvData csvData) {
        String issueCountryId = String.valueOf(lookupsService.getNationalityIdByName(csvData.documentIssueCountry()));
        String documentTypeId = lookupsService.getComboboxItemsValueByDisplayText(csvData.documentType(), 17);

        CreateOrUpdateCustomerRequestBean.Attachment attachment = new CreateOrUpdateCustomerRequestBean.Attachment(
                "", // URL - empty for CSV import
                0,  // Size
                ""  // Type
        );

        return new CreateOrUpdateCustomerRequestBean.DocumentDto(
                "IdentityDto",
                issueCountryId,
                documentTypeId,
                StringUtil.getValueOrEmpty(csvData.documentNumber()),
                1, // Default copyNumber
                DateUtil.formatDateToRenteyFormat(csvData.birthDate()), // Use birthDate as issueDate if available
                DateUtil.formatDateToRenteyFormat(csvData.documentExpireDate()),
                "Identity",
                lookupsService.getCountryNameByIsoCode(csvData.documentIssueCountry()),
                attachment,
                null
        );
    }

    /**
     * Builds an Identity Document from CSV data.
     *
     * @return DocumentDto for identity document
     */
    public CustomerDataBuilder buildCustomerDocument(List<CreateOrUpdateCustomerRequestBean.DocumentDto> documentDtos) {
        CustomerDataBuilder builder = CustomerDataBuilder.create(settingsApiBaseUrl);
        for (CreateOrUpdateCustomerRequestBean.DocumentDto documentDto : documentDtos) {
            String documentTypeId = lookupsService.getComboboxItemsValueByDisplayText(documentDto.typeName(), 17);
            String countryId = countryService.getOperationalCountryIdFromName(documentDto.issueCountry());
            CreateOrUpdateCustomerRequestBean.Attachment attachment = new CreateOrUpdateCustomerRequestBean.Attachment(
                    "", // URL - empty for CSV import
                    0,  // Size
                    ""  // Type
            );
            CreateOrUpdateCustomerRequestBean.DocumentDto documentDto1 = new CreateOrUpdateCustomerRequestBean.DocumentDto(
                    documentDto.discriminator(),
                    countryId,
                    documentTypeId,
                    documentDto.number(),
                    1, // Default copyNumber
                    DateUtil.formatDateToRenteyFormat(documentDto.issueDate()), // Use birthDate as issueDate if available
                    DateUtil.formatDateToRenteyFormat(documentDto.expiryDate()),
                    documentDto.typeName(),
                    documentDto.issueCountry(),
                    attachment,
                    null
            );


            builder.withDocument(documentDto1);
        }
        return builder;
    }

    /**
     * Builds an Identity Document with just a document number.
     * Uses default values for other fields (SAU country, default document type).
     *
     * @param documentNumber The document number
     * @return DocumentDto for identity document
     */
    public CreateOrUpdateCustomerRequestBean.DocumentDto buildIdentityDocument(String countryName, String documentNumber) {
        String countryId = countryService.getOperationalCountryIdFromName(countryName);
        String documentTypeId = lookupsService.getComboboxItemsValueByDisplayText("National ID", 17);
        if (documentTypeId == null) {
            documentTypeId = "-1"; // Default if not found
        }
        CreateOrUpdateCustomerRequestBean.Attachment attachment = new CreateOrUpdateCustomerRequestBean.Attachment(
                "", // URL - empty
                0,  // Size
                ""  // Type
        );

        return new CreateOrUpdateCustomerRequestBean.DocumentDto(
                "IdentityDto",
                countryId,
                documentTypeId,
                StringUtil.getValueOrEmpty(documentNumber),
                1, // Default copyNumber
                DateUtil.formatDateToRenteyFormat(""), // Empty date
                DateUtil.formatDateToRenteyFormat(""), // Empty date
                "Identity",
                countryName,
                attachment,
                null
        );
    }
}

