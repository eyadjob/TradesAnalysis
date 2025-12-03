package com.authorization.services;

import com.authorization.annotation.LogRequestAndResponseOnDesk;
import com.authorization.beans.AuthenticateRequest;
import com.authorization.beans.AuthenticateResponse;
import com.authorization.clients.AuthorizationClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationService.class);
    
    private final AuthorizationClient authorizationClient;

    public AuthorizationService(AuthorizationClient authorizationClient) {
        this.authorizationClient = authorizationClient;
    }

    @LogRequestAndResponseOnDesk
    public AuthenticateResponse authenticate(AuthenticateRequest request) {
        logger.info("authenticate service request: {}", request);
        return authorizationClient.authenticate(request);
    }
}

