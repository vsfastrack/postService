package com.tech.bee.postservice.advice;

import com.tech.bee.postservice.annotation.RequestMetrics;
import com.tech.bee.postservice.util.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class RequestMetricsAdvice {
    @Before("@annotation(requestMetrics)")
    public void before(JoinPoint joinPoint, RequestMetrics requestMetrics) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Class<?> targetClass = joinPoint.getTarget().getClass();
        RequestMapping requestMapping = targetClass.getAnnotation(RequestMapping.class);
        Method method = methodSignature.getMethod();
        String requestMappingValue = requestMapping.value()[0]  + WebUtils.extractRequestMappingValue(method);
        request.setAttribute("requestMapping", requestMappingValue);
        request.setAttribute("requestMethod", WebUtils.getEndpointMethod());
//        log.info("endpoint = {}  requestMethod = {} requestParams = {}",requestMappingValue , WebUtils.getEndpointMethod() , WebUtils.getRequestBody(request));
    }
}
