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

    @Autowired
    @Qualifier("settingsWebClient")
    private WebClient webClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerService customerService;
    public CreateOrUpdateCustomerResponseBean importCustomer(CreateOrUpdateCustomerRequestBean request) {

        return customerService.createOrUpdateCustomer(request);
    }


}

