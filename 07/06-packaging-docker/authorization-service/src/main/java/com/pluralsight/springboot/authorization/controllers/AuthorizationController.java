package com.pluralsight.springboot.authorization.controllers;

import com.pluralsight.springboot.authorization.services.AuthorizationService;
import com.pluralsight.springboot.beans.AuthenticateRequest;
import com.pluralsight.springboot.beans.AuthenticateResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/TokenAuth")
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    public AuthorizationController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @PostMapping(path = "/Authenticate")
    public AuthenticateResponse authenticate(@RequestBody AuthenticateRequest request) {
        return authorizationService.authenticate(request);
    }
}

