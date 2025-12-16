package com.authorization.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthenticateRequest(
        @JsonProperty("userNameOrEmailAddress") String userNameOrEmailAddress,
        @JsonProperty("password") String password,
        @JsonProperty("rememberClient") Boolean rememberClient,
        @JsonProperty("twoFactorRememberClientToken") String twoFactorRememberClientToken,
        @JsonProperty("singleSignIn") Boolean singleSignIn,
        @JsonProperty("returnUrl") String returnUrl,
        // Dynamic configuration from requester (sent to authorization-service but not to external API)
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
) {
    /**
     * Creates a request object with only authentication fields for external API calls.
     * Configuration fields are set to empty strings (they will be ignored by Jackson when serializing).
     */
    public AuthenticateRequest toApiRequest() {
        return new AuthenticateRequest(
                userNameOrEmailAddress,
                password,
                rememberClient,
                twoFactorRememberClientToken,
                singleSignIn,
                returnUrl,
                "", "", "", "", "", "", "", "", "", "", "", "", ""
        );
    }
}

