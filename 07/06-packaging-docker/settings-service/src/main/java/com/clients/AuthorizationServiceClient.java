package com.clients;

import com.beans.AuthorizationAuthenticateRequest;
import com.beans.AuthorizationAuthenticateResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface AuthorizationServiceClient {

    @PostExchange("/api/TokenAuth/Authenticate")
    AuthorizationAuthenticateResponse authenticate(@RequestBody AuthorizationAuthenticateRequest request);
}

