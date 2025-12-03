package com.services;

import com.beans.CreateOrUpdateCustomerRequestBean;
import com.beans.CreateOrUpdateCustomerResponseBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class ImportCustomerService {

    private static final Logger logger = LoggerFactory.getLogger(ImportCustomerService.class);

    private final WebClient settingsWebClient;
    private final ObjectMapper objectMapper;

    @Autowired
    CustomerService customerService;

    public ImportCustomerService(
            @Qualifier("settingsWebClient") WebClient settingsWebClient,
            ObjectMapper objectMapper) {
        this.settingsWebClient = settingsWebClient;
        this.objectMapper = objectMapper;
    }
    public CreateOrUpdateCustomerResponseBean importCustomer(CreateOrUpdateCustomerRequestBean request) {

        return customerService.createOrUpdateCustomer(request);
    }


}

