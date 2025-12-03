package com.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthenticateResponseBean(
        @JsonProperty("result") AuthResult result,
        @JsonProperty("targetUrl") String targetUrl,
        @JsonProperty("success") Boolean success,
        @JsonProperty("error") String error,
        @JsonProperty("unAuthorizedRequest") Boolean unAuthorizedRequest,
        @JsonProperty("__abp") Boolean abp
) implements ResponsePayload {
    public record AuthResult(
            @JsonProperty("accessToken") String accessToken,
            @JsonProperty("encryptedAccessToken") String encryptedAccessToken,
            @JsonProperty("expireInSeconds") Integer expireInSeconds,
            @JsonProperty("shouldResetPassword") Boolean shouldResetPassword,
            @JsonProperty("passwordResetCode") String passwordResetCode,
            @JsonProperty("userId") Integer userId,
            @JsonProperty("requiresTwoFactorVerification") Boolean requiresTwoFactorVerification,
            @JsonProperty("twoFactorAuthProviders") String twoFactorAuthProviders,
            @JsonProperty("twoFactorRememberClientToken") String twoFactorRememberClientToken,
            @JsonProperty("returnUrl") String returnUrl,
            @JsonProperty("refreshToken") String refreshToken,
            @JsonProperty("refreshTokenExpireInSeconds") Integer refreshTokenExpireInSeconds,
            @JsonProperty("c") String c
    ) {
    }
}

