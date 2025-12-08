package com.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

/**
 * Service for sending emails via Microsoft Graph API.
 */
@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    // Microsoft Graph API configuration
    private static final String CLIENT_ID = "7ac6e9eb-abc5-4eb6-8cbd-34f07e16ab9b";
    private static final String CLIENT_SECRET = "Can8Q~fxdagiHSrgUc4cU2WNfBl1PzSA4IHUPcCY";
    private static final String TENANT_ID = "5bbb3432-fada-46ae-9b16-ed4929b5d4b3";
    private static final String TOKEN_ENDPOINT = "https://login.microsoftonline.com/" + TENANT_ID + "/oauth2/v2.0/token";
    private static final String GRAPH_API_BASE_URL = "https://graph.microsoft.com/v1.0";
    private static final String SCOPE = "https://graph.microsoft.com/.default";
    private static final String RECIPIENT_EMAIL = "e.mubarak@qoad.com";
    // Note: For Microsoft Graph API, you need to send FROM a user's mailbox
    // The sender email should be a user in your Azure AD tenant
    // If RECIPIENT_EMAIL is also the sender, use it as the userId in the API call
    private static final String SENDER_EMAIL = RECIPIENT_EMAIL; // Can be changed to a different sender

    private final WebClient webClient;
    private String cachedAccessToken;
    private long tokenExpiryTime;

    public EmailService() {
        this.webClient = WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();
    }

    /**
     * Gets an OAuth2 access token using client credentials flow.
     *
     * @return Access token
     */
    private Mono<String> getAccessToken() {
        // Check if we have a valid cached token
        if (cachedAccessToken != null && System.currentTimeMillis() < tokenExpiryTime) {
            logger.debug("Using cached access token");
            return Mono.just(cachedAccessToken);
        }

        logger.info("Fetching new access token from Microsoft Azure AD");

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", CLIENT_ID);
        formData.add("client_secret", CLIENT_SECRET);
        formData.add("scope", SCOPE);
        formData.add("grant_type", "client_credentials");

        return webClient.post()
                .uri(TOKEN_ENDPOINT)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .map(tokenResponse -> {
                    cachedAccessToken = tokenResponse.accessToken;
                    // Set expiry time to 5 minutes before actual expiry for safety
                    long expiresInSeconds = tokenResponse.expiresIn != null ? tokenResponse.expiresIn : 3600;
                    tokenExpiryTime = System.currentTimeMillis() + ((expiresInSeconds - 300) * 1000);
                    logger.info("Successfully obtained access token, expires in {} seconds", expiresInSeconds);
                    return cachedAccessToken;
                })
                .doOnError(error -> logger.error("Error obtaining access token", error));
    }

    /**
     * Sends an email using Microsoft Graph API.
     *
     * @param subject Email subject
     * @param body    Email body (HTML or plain text)
     * @param isHtml  Whether the body is HTML
     * @return Mono that completes when email is sent
     */
    public Mono<Void> sendEmail(String subject, String body, boolean isHtml) {
        logger.info("Preparing to send email to: {}", RECIPIENT_EMAIL);

        return getAccessToken()
                .flatMap(accessToken -> {
                    EmailRequest emailRequest = createEmailRequest(subject, body, isHtml);

                    WebClient graphClient = WebClient.builder()
                            .baseUrl(GRAPH_API_BASE_URL)
                            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .build();

                    logger.info("Sending email via Microsoft Graph API from: {}", SENDER_EMAIL);

                    return graphClient.post()
                            .uri("/users/{userId}/sendMail", SENDER_EMAIL)
                            .bodyValue(emailRequest)
                            .retrieve()
                            .bodyToMono(String.class)
                            .doOnSuccess(response -> logger.info("Email sent successfully from {} to {}", SENDER_EMAIL, RECIPIENT_EMAIL))
                            .doOnError(error -> logger.error("Error sending email from {} to {}", SENDER_EMAIL, RECIPIENT_EMAIL, error))
                            .then();
                });
    }

    /**
     * Sends an email with HTML body.
     *
     * @param subject Email subject
     * @param htmlBody HTML email body
     * @return Mono that completes when email is sent
     */
    public Mono<Void> sendHtmlEmail(String subject, String htmlBody) {
        return sendEmail(subject, htmlBody, true);
    }

    /**
     * Sends an email with plain text body.
     *
     * @param subject Email subject
     * @param textBody Plain text email body
     * @return Mono that completes when email is sent
     */
    public Mono<Void> sendTextEmail(String subject, String textBody) {
        return sendEmail(subject, textBody, false);
    }

    /**
     * Creates an email request object for Microsoft Graph API.
     */
    private EmailRequest createEmailRequest(String subject, String body, boolean isHtml) {
        EmailMessage message = new EmailMessage();
        message.subject = subject;
        message.body = new EmailBody();
        message.body.contentType = isHtml ? "HTML" : "Text";
        message.body.content = body;

        EmailRecipient toRecipient = new EmailRecipient();
        toRecipient.emailAddress = new EmailAddress();
        toRecipient.emailAddress.address = RECIPIENT_EMAIL;
        toRecipient.emailAddress.name = "Recipient";

        message.toRecipients = List.of(toRecipient);

        EmailRequest request = new EmailRequest();
        request.message = message;
        request.saveToSentItems = true;

        return request;
    }

    // Inner classes for JSON serialization

    private static class TokenResponse {
        @JsonProperty("access_token")
        String accessToken;

        @JsonProperty("expires_in")
        Integer expiresIn;

        @JsonProperty("token_type")
        String tokenType;
    }

    private static class EmailRequest {
        @JsonProperty("message")
        EmailMessage message;

        @JsonProperty("saveToSentItems")
        boolean saveToSentItems;
    }

    private static class EmailMessage {
        @JsonProperty("subject")
        String subject;

        @JsonProperty("body")
        EmailBody body;

        @JsonProperty("toRecipients")
        List<EmailRecipient> toRecipients;
    }

    private static class EmailBody {
        @JsonProperty("contentType")
        String contentType;

        @JsonProperty("content")
        String content;
    }

    private static class EmailRecipient {
        @JsonProperty("emailAddress")
        EmailAddress emailAddress;
    }

    private static class EmailAddress {
        @JsonProperty("address")
        String address;

        @JsonProperty("name")
        String name;
    }

    /**
     * Main method for testing the email service.
     */
    public static void main(String[] args) {
        logger.info("Starting EmailService test...");

        EmailService emailService = new EmailService();

        // Test sending a simple text email
        String subject = "Test Email from Rentey Service";
        String body = "This is a test email sent from the Rentey Service application.\n\n" +
                "If you receive this email, the email service is working correctly.";

        emailService.sendTextEmail(subject, body)
                .doOnSuccess(v -> {
                    logger.info("Test email sent successfully!");
                    System.out.println("SUCCESS: Test email sent to " + RECIPIENT_EMAIL);
                })
                .doOnError(error -> {
                    logger.error("Failed to send test email", error);
                    System.err.println("ERROR: Failed to send test email: " + error.getMessage());
                    error.printStackTrace();
                })
                .block(Duration.ofSeconds(30));

        // Test sending an HTML email
        String htmlSubject = "Test HTML Email from Rentey Service";
        String htmlBody = "<html><body>" +
                "<h1>Test Email</h1>" +
                "<p>This is a <b>test email</b> sent from the Rentey Service application.</p>" +
                "<p>If you receive this email, the email service is working correctly.</p>" +
                "</body></html>";

        emailService.sendHtmlEmail(htmlSubject, htmlBody)
                .doOnSuccess(v -> {
                    logger.info("Test HTML email sent successfully!");
                    System.out.println("SUCCESS: Test HTML email sent to " + RECIPIENT_EMAIL);
                })
                .doOnError(error -> {
                    logger.error("Failed to send test HTML email", error);
                    System.err.println("ERROR: Failed to send test HTML email: " + error.getMessage());
                    error.printStackTrace();
                })
                .block(Duration.ofSeconds(30));

        logger.info("EmailService test completed.");
    }
}

