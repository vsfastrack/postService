package com.tech.bee.postservice.advice;

import com.tech.bee.postservice.annotation.RequestMetrics;
import com.tech.bee.postservice.annotation.TransactionId;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.UUID;

@Aspect
@Component
public class RequestMetricsAdvice {
    @Before("@annotation(requestMetrics)")
    public void before(JoinPoint joinPoint, RequestMetrics requestMetrics) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Class<?> targetClass = joinPoint.getTarget().getClass();
        RequestMapping requestMapping = targetClass.getAnnotation(RequestMapping.class);
        Method method = methodSignature.getMethod();
        String requestMappingValue = requestMapping.value()[0]  + extractRequestMappingValue(method);
        request.setAttribute("requestMapping", requestMappingValue);
        request.setAttribute("requestMethod", getEndpointMethod());
    }

    private String extractRequestMappingValue(Method method) {
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
    private String getEndpointMethod(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getMethod();
    }
}
