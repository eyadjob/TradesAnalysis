package com.beans;

public record AuthorizationAuthenticateResponseRecord(
        AuthResult result,
        String targetUrl,
        Boolean success,
        String error,
        Boolean unAuthorizedRequest,
        Boolean abp
) {
    public record AuthResult(
            String accessToken,
            String encryptedAccessToken,
            Integer expireInSeconds,
            Boolean shouldResetPassword,
            String passwordResetCode,
            Integer userId,
            Boolean requiresTwoFactorVerification,
            String twoFactorAuthProviders,
            String twoFactorRememberClientToken,
            String returnUrl,
            String refreshToken,
            Integer refreshTokenExpireInSeconds,
            String c
    ) {
    }
}

