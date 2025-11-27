package com.beans;

public record AuthorizationAuthenticateRequestRecord(
        String userNameOrEmailAddress,
        String password,
        Boolean rememberClient,
        String twoFactorRememberClientToken,
        Boolean singleSignIn,
        String returnUrl
) {
}

