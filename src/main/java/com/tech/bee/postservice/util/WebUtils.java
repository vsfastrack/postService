package com.tech.bee.postservice.util;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

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
}
