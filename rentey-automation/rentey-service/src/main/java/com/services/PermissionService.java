package com.services;

import com.annotation.LogExecutionTime;
import com.beans.general.AbpResponseBean;
import com.beans.customer.CreateOrUpdateRoleRequestBean;
import com.beans.user.GetAllPermissionsResponseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class PermissionService {

    @Autowired
    @Qualifier("settingsWebClient")
    private WebClient settingsWebClient;

    @Autowired
    @Qualifier("apiBasePath")
    private String apiBasePath;

    @LogExecutionTime
    public GetAllPermissionsResponseBean getAllPermissions() {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(apiBasePath + "/Permission/GetAllPermissions")
                .retrieve()
                .bodyToMono(GetAllPermissionsResponseBean.class)
                .block();
    }

    @LogExecutionTime
    public AbpResponseBean createOrUpdateRole(CreateOrUpdateRoleRequestBean request) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.post()
                .uri(apiBasePath + "/Role/CreateOrUpdateRole")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AbpResponseBean.class)
                .block();
    }
}

