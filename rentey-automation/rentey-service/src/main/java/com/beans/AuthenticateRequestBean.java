package com.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthenticateRequestBean(
        @JsonProperty("userNameOrEmailAddress") String userNameOrEmailAddress,
        @JsonProperty("password") String password,
        @JsonProperty("rememberClient") Boolean rememberClient,
        @JsonProperty("twoFactorRememberClientToken") String twoFactorRememberClientToken,
        @JsonProperty("singleSignIn") Boolean singleSignIn,
        @JsonProperty("returnUrl") String returnUrl
) implements RequestPayload {
}

