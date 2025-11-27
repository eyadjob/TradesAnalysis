package com.pluralsight.springboot.authorization.services;

import com.pluralsight.springboot.authorization.clients.AuthorizationClient;
import com.pluralsight.springboot.beans.AuthenticateRequest;
import com.pluralsight.springboot.beans.AuthenticateResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    private final AuthorizationClient authorizationClient;

    public AuthorizationService(AuthorizationClient authorizationClient) {
        this.authorizationClient = authorizationClient;
    }

    public AuthenticateResponse authenticate(AuthenticateRequest request) {
        return authorizationClient.authenticate(request);
    }
}

