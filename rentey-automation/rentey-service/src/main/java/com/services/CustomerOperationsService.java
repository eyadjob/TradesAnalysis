package com.services;

import com.beans.customer.CreateOrUpdateCustomerRequestBean;
import com.beans.customer.CreateOrUpdateCustomerResponseBean;
import com.builders.CustomerDataBuilder;
import com.util.CustomerCsvImportUtil;
import com.util.DateUtil;
import com.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerOperationsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerOperationsService.class);

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


    public CreateOrUpdateCustomerResponseBean createCustomerWithRandomData(String countryName) {
        CreateOrUpdateCustomerRequestBean createOrUpdateCustomerRequestBean = buildRequestForCustomerCreation(countryName);
        return  customerService.createOrUpdateCustomer(createOrUpdateCustomerRequestBean);
    }


    /**
     * Builds CreateOrUpdateCustomerRequestBean from CustomerCsvData.
     *
     * @return CreateOrUpdateCustomerRequestBean populated with CSV data
     */
    private CreateOrUpdateCustomerRequestBean buildRequestForCustomerCreation(String countryName) {
        CustomerDataBuilder builder = CustomerDataBuilder.create();
        String countryId = countryService.getOperationalCountryIdFromName(countryName);
        String firstName = StringUtil.generateRandomStringWithPrefix("Eyad Automation", 10);
        String secondName = StringUtil.generateRandomStringWithPrefix("Eyad Automation", 10);
        String thirdName = StringUtil.generateRandomStringWithPrefix("Eyad Automation", 10);
        String phoneNumber = "966-51" + StringUtil.generateRandomNumber(7);
        String documentNumber = StringUtil.generateRandomNumber(10);


        // Map Full Name
        builder.withFullName(
                firstName,
                secondName,
                thirdName
        );

        builder.withContactInformation(
                phoneNumber,
                firstName + "@iyelo.com"
        );

        builder.withBasicInformation(
                countryId,
                lookupsService.getComboboxItemsValueByDisplayText("Male", 6),
                DateUtil.formatDateToRenteyFormat("withBasicInformation")
        );

        builder.withAddress(countryId, -1); // -1 for cityId as default
        builder.clearDocuments();
        CreateOrUpdateCustomerRequestBean.DocumentDto identityDocument = customerService.buildCustomerDocument("IdentityDto", countryName, "Identity",countryId,documentNumber,"1989/01/15","2035/01/20");
        builder.withDocument(identityDocument);

        return builder.build();
    }

}
