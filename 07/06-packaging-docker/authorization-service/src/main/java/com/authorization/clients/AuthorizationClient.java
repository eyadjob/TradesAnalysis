package com.authorization.clients;

import com.authorization.beans.AuthenticateResponse;
import com.pluralsight.springboot.beans.AuthenticateRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface AuthorizationClient {

    @PostExchange("/webapigw/api/TokenAuth/Authenticate")
    AuthenticateResponse authenticate(@RequestBody AuthenticateRequest request);
}

