package com.services;

import com.beans.UploadBase64FileRequestBean;
import com.beans.UploadBase64FileResponseBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class VehicleService {

    private final WebClient settingsWebClient;
    private final AuthorizationTokenService authorizationTokenService;

    public VehicleService(
            @Qualifier("settingsWebClient") WebClient settingsWebClient,
            AuthorizationTokenService authorizationTokenService) {
        this.settingsWebClient = settingsWebClient;
        this.authorizationTokenService = authorizationTokenService;
    }

    /**
     * Upload a base64 encoded file.
     * This method automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param request The request containing the base64 encoded file data.
     * @return The response containing the uploaded file information.
     */
    public UploadBase64FileResponseBean uploadBase64File(UploadBase64FileRequestBean request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null.");
        }
        
        if (request.data() == null || request.data().trim().isEmpty()) {
            throw new IllegalArgumentException("File data cannot be null or empty.");
        }
        
        String refreshToken = authorizationTokenService.getRefreshToken();
        String authorization = "Bearer " + refreshToken;
        
        return settingsWebClient.post()
                .uri("/webapigw/api/FileUpload/UploadBase64File")
                .header("Authorization", authorization)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(UploadBase64FileResponseBean.class)
                .block();
    }
}

