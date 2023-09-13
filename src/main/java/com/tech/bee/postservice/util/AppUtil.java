package com.tech.bee.postservice.util;

import com.tech.bee.postservice.common.ErrorDTO;
import com.tech.bee.postservice.constants.ApiConstants;
import com.tech.bee.postservice.enums.Enums;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class AppUtil {

    public String generateIdentifier(String prefix){
        StringBuilder stringBuilder = new StringBuilder();
        String currentTimeStamp = LocalDateTime.now().toString().replace("-","");
        String timeStamp = currentTimeStamp.replaceAll(":","");
        String identifier = timeStamp.replace(".","");
        String finalIdentifier = identifier.replace("T","");
        return stringBuilder.append(prefix).append(finalIdentifier).toString();
    }

    private ErrorDTO buildError(Enums.ErrorCategory category , String key , String code , String message){
        return ErrorDTO.builder().category(category).key(key)
                .code(code).message(message).build();
    }

    public ErrorDTO buildResourceNotFoundError(String key){
        return buildError(
                Enums.ErrorCategory.BUSINESS_VALIDATION_ERROR ,
                key,
                ApiConstants.ErrorCodeConstants.CODE_RESOURCE_NOT_FOUND ,
                ApiConstants.ErrorMsgConstants.MESSAGE_RESOURCE_NOT_FOUND
        );
    }

    public ErrorDTO buildValidationErrorError(String key){
        return buildError(
                Enums.ErrorCategory.VALIDATION_ERROR ,
                key,
                ApiConstants.ErrorCodeConstants.CODE_FIELD_CANNOT_BE_EMPTY ,
                ApiConstants.ErrorMsgConstants.MESSAGE_FIELD_CANNOT_BE_EMPTY
        );
    }

    public ErrorDTO buildResourceConflictError(String key){
        return buildError(
                Enums.ErrorCategory.BUSINESS_VALIDATION_ERROR ,
                key,
                ApiConstants.ErrorCodeConstants.CODE_RESOURCE_CONFLICT,
                ApiConstants.ErrorMsgConstants.MESSAGE_RESOURCE_CONFLICT
        );
    }

}
