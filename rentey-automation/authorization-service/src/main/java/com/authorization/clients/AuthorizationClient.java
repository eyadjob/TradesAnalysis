package com.authorization.clients;

import com.authorization.annotation.LogRequestAndResponseOnDesk;
import com.authorization.beans.AuthenticateRequest;
import com.authorization.beans.AuthenticateResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface AuthorizationClient {


    @LogRequestAndResponseOnDesk
    @PostExchange("/webapigw/api/TokenAuth/Authenticate")
    AuthenticateResponse authenticate(@RequestBody AuthenticateRequest request);
}

