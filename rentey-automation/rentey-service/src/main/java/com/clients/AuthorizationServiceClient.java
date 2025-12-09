package com.clients;

import com.beans.authentication.AuthenticateRequestBean;
import com.beans.authentication.AuthenticateResponseBean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface AuthorizationServiceClient {

    @PostExchange("/api/TokenAuth/Authenticate")
    AuthenticateResponseBean authenticate(@RequestBody AuthenticateRequestBean request);
}

