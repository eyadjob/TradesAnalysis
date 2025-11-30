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
    private final AuthorizationTokenService authorizationTokenService;

    public PermissionService(
            @Qualifier("settingsWebClient") WebClient settingsWebClient,
            AuthorizationTokenService authorizationTokenService) {
        this.settingsWebClient = settingsWebClient;
        this.authorizationTokenService = authorizationTokenService;
    }

    public GetAllPermissionsResponseBean getAllPermissions() {
        String refreshToken = authorizationTokenService.getRefreshToken();
        String authorization = "Bearer " + refreshToken;
        
        return settingsWebClient.get()
                .uri("/webapigw/api/services/app/Permission/GetAllPermissions")
                .header("Authorization", authorization)
                .retrieve()
                .bodyToMono(GetAllPermissionsResponseBean.class)
                .block();
    }

    public AbpResponseBean createOrUpdateRole(CreateOrUpdateRoleRequestBean request) {
        String refreshToken = authorizationTokenService.getRefreshToken();
        String authorization = "Bearer " + refreshToken;
        
        return settingsWebClient.post()
                .uri("/webapigw/api/services/app/Role/CreateOrUpdateRole")
                .header("Authorization", authorization)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AbpResponseBean.class)
                .block();
    }
}

