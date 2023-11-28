package com.tech.bee.postservice.advice;

import com.tech.bee.postservice.common.ApiResponseDTO;
import com.tech.bee.postservice.exception.BaseCustomException;
import com.tech.bee.postservice.metrics.RequestMetricCounter;
import com.tech.bee.postservice.util.WebUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final RequestMetricCounter requestMetricCounter;

    @ExceptionHandler(BaseCustomException.class)
    public ResponseEntity<ApiResponseDTO> apiExceptionHandler(BaseCustomException exception){
        ApiResponseDTO apiResponse = ApiResponseDTO.builder().
                errors(exception.getErrors()).build();
        apiResponse.setTimeStamp(getCurrentTimestamp());
        apiResponse.setTransactionId(getTransactionId());
        requestMetricCounter.increment(WebUtils.getRequestEndpoint() ,
                WebUtils.getRequestMethod() , exception.getHttpStatus().value());
        return new ResponseEntity<>(apiResponse , exception.getHttpStatus());
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
