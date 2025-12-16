package com.beans.authentication;

import com.beans.interfaces.RequestPayload;
import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthenticateRequestBean(
        @JsonProperty("userNameOrEmailAddress") String userNameOrEmailAddress,
        @JsonProperty("password") String password,
        @JsonProperty("rememberClient") Boolean rememberClient,
        @JsonProperty("twoFactorRememberClientToken") String twoFactorRememberClientToken,
        @JsonProperty("singleSignIn") Boolean singleSignIn,
        @JsonProperty("returnUrl") String returnUrl,
        // Dynamic configuration for authorization-service
        @JsonProperty("baseUrl") String baseUrl,
        @JsonProperty("tenantId") String tenantId,
        @JsonProperty("userAgent") String userAgent,
        @JsonProperty("accept") String accept,
        @JsonProperty("acceptLanguage") String acceptLanguage,
        @JsonProperty("acceptEncoding") String acceptEncoding,
        @JsonProperty("pragma") String pragma,
        @JsonProperty("cacheControl") String cacheControl,
        @JsonProperty("expires") String expires,
        @JsonProperty("xRequestedWith") String xRequestedWith,
        @JsonProperty("aspnetcoreCulture") String aspnetcoreCulture,
        @JsonProperty("origin") String origin,
        @JsonProperty("referer") String referer
) implements RequestPayload {
}

