package com.services;

import com.beans.AbpResponseBean;
import com.beans.CreateOrUpdateRoleRequestBean;
import com.beans.GetAllPermissionsResponseBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class PermissionService {

    private final WebClient settingsWebClient;

    public PermissionService(
            @Qualifier("settingsWebClient") WebClient settingsWebClient) {
        this.settingsWebClient = settingsWebClient;
    }

    public GetAllPermissionsResponseBean getAllPermissions() {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri("/webapigw/api/services/app/Permission/GetAllPermissions")
                .retrieve()
                .bodyToMono(GetAllPermissionsResponseBean.class)
                .block();
    }

    public AbpResponseBean createOrUpdateRole(CreateOrUpdateRoleRequestBean request) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.post()
                .uri("/webapigw/api/services/app/Role/CreateOrUpdateRole")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AbpResponseBean.class)
                .block();
    }
}

