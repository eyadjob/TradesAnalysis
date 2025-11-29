package com.authorization.services;

import com.authorization.beans.AuthenticateResponse;
import com.authorization.clients.AuthorizationClient;
import com.pluralsight.springboot.beans.AuthenticateRequest;
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

