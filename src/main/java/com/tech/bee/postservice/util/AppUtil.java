package com.tech.bee.postservice.util;

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

}
