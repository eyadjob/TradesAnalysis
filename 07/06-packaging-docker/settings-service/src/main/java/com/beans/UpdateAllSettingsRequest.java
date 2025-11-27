package com.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UpdateAllSettingsRequest(
        @JsonProperty("general") GeneralSettings general,
        @JsonProperty("userManagement") UserManagementSettings userManagement,
        @JsonProperty("email") EmailSettings email,
        @JsonProperty("ldap") LdapSettings ldap,
        @JsonProperty("security") SecuritySettings security,
        @JsonProperty("billing") BillingSettings billing,
        @JsonProperty("otherSettings") OtherSettings otherSettings,
        @JsonProperty("externalLoginProviderSettings") ExternalLoginProviderSettings externalLoginProviderSettings
) {
    public record GeneralSettings(
            @JsonProperty("timezone") String timezone,
            @JsonProperty("timezoneForComparison") String timezoneForComparison
    ) {
    }

    public record UserManagementSettings(
            @JsonProperty("allowSelfRegistration") Boolean allowSelfRegistration,
            @JsonProperty("isNewRegisteredUserActiveByDefault") Boolean isNewRegisteredUserActiveByDefault,
            @JsonProperty("isEmailConfirmationRequiredForLogin") Boolean isEmailConfirmationRequiredForLogin,
            @JsonProperty("useCaptchaOnRegistration") Boolean useCaptchaOnRegistration,
            @JsonProperty("useCaptchaOnLogin") Boolean useCaptchaOnLogin,
            @JsonProperty("isCookieConsentEnabled") Boolean isCookieConsentEnabled,
            @JsonProperty("isQuickThemeSelectEnabled") Boolean isQuickThemeSelectEnabled,
            @JsonProperty("allowUsingGravatarProfilePicture") Boolean allowUsingGravatarProfilePicture,
            @JsonProperty("sessionTimeOutSettings") SessionTimeOutSettings sessionTimeOutSettings
    ) {
        public record SessionTimeOutSettings(
                @JsonProperty("isEnabled") Boolean isEnabled,
                @JsonProperty("timeOutSecond") Integer timeOutSecond,
                @JsonProperty("showTimeOutNotificationSecond") Integer showTimeOutNotificationSecond,
                @JsonProperty("showLockScreenWhenTimedOut") Boolean showLockScreenWhenTimedOut
        ) {
        }
    }

    public record EmailSettings(
            @JsonProperty("useHostDefaultEmailSettings") Boolean useHostDefaultEmailSettings,
            @JsonProperty("defaultFromAddress") String defaultFromAddress,
            @JsonProperty("defaultFromDisplayName") String defaultFromDisplayName,
            @JsonProperty("smtpHost") String smtpHost,
            @JsonProperty("smtpPort") Integer smtpPort,
            @JsonProperty("smtpUserName") String smtpUserName,
            @JsonProperty("smtpPassword") String smtpPassword,
            @JsonProperty("smtpDomain") String smtpDomain,
            @JsonProperty("smtpEnableSsl") Boolean smtpEnableSsl,
            @JsonProperty("smtpUseDefaultCredentials") Boolean smtpUseDefaultCredentials
    ) {
    }

    public record LdapSettings(
            @JsonProperty("isModuleEnabled") Boolean isModuleEnabled,
            @JsonProperty("isEnabled") Boolean isEnabled,
            @JsonProperty("domain") String domain,
            @JsonProperty("userName") String userName,
            @JsonProperty("password") String password,
            @JsonProperty("useSsl") Boolean useSsl
    ) {
    }

    public record SecuritySettings(
            @JsonProperty("allowOneConcurrentLoginPerUser") Boolean allowOneConcurrentLoginPerUser,
            @JsonProperty("useDefaultPasswordComplexitySettings") Boolean useDefaultPasswordComplexitySettings,
            @JsonProperty("passwordComplexity") PasswordComplexity passwordComplexity,
            @JsonProperty("defaultPasswordComplexity") PasswordComplexity defaultPasswordComplexity,
            @JsonProperty("userLockOut") UserLockOutSettings userLockOut,
            @JsonProperty("twoFactorLogin") TwoFactorLoginSettings twoFactorLogin
    ) {
        public record PasswordComplexity(
                @JsonProperty("allowedMinimumLength") Integer allowedMinimumLength,
                @JsonProperty("requireDigit") Boolean requireDigit,
                @JsonProperty("requireLowercase") Boolean requireLowercase,
                @JsonProperty("requireNonAlphanumeric") Boolean requireNonAlphanumeric,
                @JsonProperty("requireUppercase") Boolean requireUppercase,
                @JsonProperty("requiredLength") Integer requiredLength
        ) {
        }

        public record UserLockOutSettings(
                @JsonProperty("isEnabled") Boolean isEnabled,
                @JsonProperty("maxFailedAccessAttemptsBeforeLockout") Integer maxFailedAccessAttemptsBeforeLockout,
                @JsonProperty("defaultAccountLockoutSeconds") Integer defaultAccountLockoutSeconds
        ) {
        }

        public record TwoFactorLoginSettings(
                @JsonProperty("isEnabledForApplication") Boolean isEnabledForApplication,
                @JsonProperty("isEnabled") Boolean isEnabled,
                @JsonProperty("isEmailProviderEnabled") Boolean isEmailProviderEnabled,
                @JsonProperty("isSmsProviderEnabled") Boolean isSmsProviderEnabled,
                @JsonProperty("isRememberBrowserEnabled") Boolean isRememberBrowserEnabled,
                @JsonProperty("isGoogleAuthenticatorEnabled") Boolean isGoogleAuthenticatorEnabled
        ) {
        }
    }

    public record BillingSettings(
            @JsonProperty("legalName") String legalName,
            @JsonProperty("address") String address,
            @JsonProperty("taxVatNo") String taxVatNo
    ) {
    }

    public record OtherSettings(
            @JsonProperty("isQuickThemeSelectEnabled") Boolean isQuickThemeSelectEnabled
    ) {
    }

    public record ExternalLoginProviderSettings(
            @JsonProperty("facebook_IsDeactivated") Boolean facebookIsDeactivated,
            @JsonProperty("facebook") FacebookSettings facebook,
            @JsonProperty("google_IsDeactivated") Boolean googleIsDeactivated,
            @JsonProperty("google") GoogleSettings google,
            @JsonProperty("twitter_IsDeactivated") Boolean twitterIsDeactivated,
            @JsonProperty("twitter") TwitterSettings twitter,
            @JsonProperty("microsoft_IsDeactivated") Boolean microsoftIsDeactivated,
            @JsonProperty("microsoft") MicrosoftSettings microsoft,
            @JsonProperty("openIdConnect_IsDeactivated") Boolean openIdConnectIsDeactivated,
            @JsonProperty("openIdConnect") OpenIdConnectSettings openIdConnect,
            @JsonProperty("openIdConnectClaimsMapping") java.util.List<ClaimsMapping> openIdConnectClaimsMapping,
            @JsonProperty("wsFederation_IsDeactivated") Boolean wsFederationIsDeactivated,
            @JsonProperty("wsFederation") WsFederationSettings wsFederation,
            @JsonProperty("wsFederationClaimsMapping") java.util.List<ClaimsMapping> wsFederationClaimsMapping
    ) {
        public record FacebookSettings(
                @JsonProperty("appId") String appId,
                @JsonProperty("appSecret") String appSecret
        ) {
        }

        public record GoogleSettings(
                @JsonProperty("clientId") String clientId,
                @JsonProperty("clientSecret") String clientSecret,
                @JsonProperty("userInfoEndpoint") String userInfoEndpoint
        ) {
        }

        public record TwitterSettings(
                @JsonProperty("consumerKey") String consumerKey,
                @JsonProperty("consumerSecret") String consumerSecret
        ) {
        }

        public record MicrosoftSettings(
                @JsonProperty("clientId") String clientId,
                @JsonProperty("clientSecret") String clientSecret
        ) {
        }

        public record OpenIdConnectSettings(
                @JsonProperty("clientId") String clientId,
                @JsonProperty("clientSecret") String clientSecret,
                @JsonProperty("authority") String authority,
                @JsonProperty("loginUrl") String loginUrl,
                @JsonProperty("validateIssuer") Boolean validateIssuer,
                @JsonProperty("responseType") String responseType
        ) {
        }

        public record WsFederationSettings(
                @JsonProperty("clientId") String clientId,
                @JsonProperty("tenant") String tenant,
                @JsonProperty("metaDataAddress") String metaDataAddress,
                @JsonProperty("wtrealm") String wtrealm,
                @JsonProperty("authority") String authority
        ) {
        }

        public record ClaimsMapping(
                @JsonProperty("claim") String claim,
                @JsonProperty("key") String key
        ) {
        }
    }
}

