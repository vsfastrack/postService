package com.tech.bee.postservice.util;

import com.tech.bee.postservice.common.ErrorDTO;
import com.tech.bee.postservice.constants.ApiConstants;
import com.tech.bee.postservice.enums.Enums;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;

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

    public ErrorDTO buildError(Enums.ErrorCategory category , String code , String message){
        return ErrorDTO.builder().category(category)
                .code(code).message(message).build();
    }

}
