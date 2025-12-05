package com.services;

import com.beans.UploadBase64FileRequestBean;
import com.beans.UploadBase64FileResponseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class VehicleService {

    @Autowired
    @Qualifier("settingsWebClient")
    private WebClient settingsWebClient;

    /**
     * Upload a base64 encoded file.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
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
        
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.post()
                .uri("/webapigw/api/FileUpload/UploadBase64File")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(UploadBase64FileResponseBean.class)
                .block();
    }
}

