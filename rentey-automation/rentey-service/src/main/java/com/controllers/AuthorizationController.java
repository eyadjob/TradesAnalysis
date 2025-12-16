package com.controllers;

import com.beans.authentication.AuthenticateRequestBean;
import com.beans.authentication.AuthenticateResponseBean;
import com.services.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.controllers.ApiPaths.*;

/**
 * REST controller for authorization and authentication operations.
 */
@RestController
@RequestMapping(path = BASE_PATH_WITHOUT_SERVICE)
public class AuthorizationController {

    @Autowired
    private AuthorizationService authorizationService;

    /**
     * Authenticate user and get access token.
     * This endpoint directly calls the external API to authenticate a user.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     * 
     * @param request The authentication request containing username/email, password, and other authentication parameters.
     * @return The response containing access token, refresh token, and user information.
     */
    @PostMapping(path = TOKEN_AUTH_AUTHENTICATE, consumes = "application/json", produces = "application/json")
    public AuthenticateResponseBean authenticate(
            @RequestBody(required = true) AuthenticateRequestBean request) {

        if (request == null) {
            throw new IllegalArgumentException("Request body is required and cannot be null.");
        }

        return authorizationService.authenticate(request);
    }
}
