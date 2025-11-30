package com.controllers;

import com.beans.UploadBase64FileRequestBean;
import com.beans.UploadBase64FileResponseBean;
import com.services.VehicleService;
import org.springframework.web.bind.annotation.*;

import static com.controllers.ApiPaths.*;

@RestController
@RequestMapping(path = BASE_PATH)
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    /**
     * Upload a base64 encoded file.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param request The request containing the base64 encoded file data.
     * @return The response containing the uploaded file information.
     */
    @PostMapping(path = FILE_UPLOAD_BASE64, consumes = "application/json", produces = "application/json")
    public UploadBase64FileResponseBean uploadBase64File(
            @RequestBody(required = true) UploadBase64FileRequestBean request) {

        if (request == null) {
            throw new IllegalArgumentException("Request body is required and cannot be null.");
        }

        if (request.data() == null || request.data().trim().isEmpty()) {
            throw new IllegalArgumentException("File data is required and cannot be null or empty.");
        }

        return vehicleService.uploadBase64File(request);
    }
}

