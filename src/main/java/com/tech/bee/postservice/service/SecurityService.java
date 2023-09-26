package com.tech.bee.postservice.service;

import com.tech.bee.postservice.common.ErrorDTO;
import com.tech.bee.postservice.constants.ApiConstants;
import com.tech.bee.postservice.enums.Enums;
import org.keycloak.KeycloakPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SecurityService {

    public String getCurrentLoggedInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof KeycloakPrincipal) {
            KeycloakPrincipal principal = (KeycloakPrincipal) authentication.getPrincipal();
            return principal.getName();
        }
        return null;
    }


    public List<ErrorDTO> validateOwnership(String createdBy){
        List<ErrorDTO> validationErrors = new ArrayList<>();
        String currentLoggedInUser = getCurrentLoggedInUser();
        if(!currentLoggedInUser.equals(createdBy))
            validationErrors.add(ErrorDTO.builder()
                    .code(ApiConstants.ErrorCodeConstants.CODE_OPERATION_FORBIDDEN)
                    .message(ApiConstants.ErrorMsgConstants.MESSAGE_OPERATION_FORBIDDEN)
                    .key(ApiConstants.KeyConstants.KEY_USER)
                    .category(Enums.ErrorCategory.BUSINESS_VALIDATION_ERROR).build());
        return validationErrors;
    }
}
