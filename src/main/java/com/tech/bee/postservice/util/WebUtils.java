package com.tech.bee.postservice.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.lang.reflect.Method;
import java.util.Objects;

@Component
public class WebUtils {


    public static String getRequestEndpoint(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        // Retrieve the transaction ID from the transaction context of the request
        return  (String) request.getAttribute("requestMapping");
    }

    public static String getRequestMethod(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        // Retrieve the transaction ID from the transaction context of the request
        return  (String) request.getAttribute("requestMethod");
    }

    public static String getRequestBody(HttpServletRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            char[] buffer = new char[1024];
            int bytesRead;
            while ((bytesRead = reader.read(buffer)) != -1) {
                stringBuilder.append(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static String extractRequestMappingValue(Method method) {
        String endpointMethod = getEndpointMethod();
        String result = StringUtils.EMPTY;
        switch(endpointMethod){
            case "GET":
                GetMapping getMapping = method.getAnnotation(GetMapping.class);
                if(Objects.nonNull(getMapping) && getMapping.value().length > 0)
                    result = getMapping.value()[0];
                break;
            case "POST":
                PostMapping postMapping = method.getAnnotation(PostMapping.class);
                if(Objects.nonNull(postMapping) && postMapping.value().length > 0)
                    result = postMapping.value()[0];
                break;
            case "PATCH":
                PatchMapping patchMapping = method.getAnnotation(PatchMapping.class);
                if(Objects.nonNull(patchMapping) && patchMapping.value().length > 0)
                    result = patchMapping.value()[0];
                break;
            case "DELETE":
                DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);
                if(Objects.nonNull(deleteMapping) && deleteMapping.value().length > 0)
                    result = deleteMapping.value()[0];
                break;
        }
        return result;
    }
    public static String getEndpointMethod(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getMethod();
    }
}
