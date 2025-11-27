package com.pluralsight.springboot.authorization.clients;


import com.pluralsight.springboot.beans.AuthenticateRequest;
import com.pluralsight.springboot.beans.AuthenticateResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface AuthorizationClient {

    @PostExchange("/webapigw/api/TokenAuth/Authenticate")
    AuthenticateResponse authenticate(@RequestBody AuthenticateRequest request);
}

