package com.tech.bee.postservice.advice;

import com.tech.bee.postservice.common.ApiResponseDTO;
import com.tech.bee.postservice.metrics.RequestMetricCounter;
import com.tech.bee.postservice.util.AppUtil;
import com.tech.bee.postservice.util.web.WebUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Aspect
@Component
@RequiredArgsConstructor
@Log4j2
public class ResponseEnrichmentAdvice {
    private final RequestMetricCounter requestMetricCounter;
    @AfterReturning(pointcut = "execution(* com.tech.bee.postservice.resources.*.*(..))", returning = "response")
    public void afterReturningFromController(ResponseEntity response) {
        // Set the timestamp in the response body
        if (response.getBody() instanceof ApiResponseDTO) {
            ApiResponseDTO apiResponse = (ApiResponseDTO) response.getBody();
            apiResponse.setTimeStamp(getCurrentTimestamp());
            apiResponse.setTransactionId(getTransactionId());
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            String requestMappingValue = (String) request.getAttribute("requestMapping");
            log.info("endpoint = {}  requestMethod = {} requestParams = {}",requestMappingValue , WebUtils.getEndpointMethod() , AppUtil.getAsJsonString(apiResponse.getContent()));
        }
        requestMetricCounter.increment(WebUtils.getRequestEndpoint() ,
                WebUtils.getRequestMethod() , response.getStatusCode().value());
    }

    private String getCurrentTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }
    private String getTransactionId(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        // Retrieve the transaction ID from the transaction context of the request
        return  (String) request.getAttribute("transactionId");
    }
}
