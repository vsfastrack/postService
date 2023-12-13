package com.tech.bee.postservice.advice;

import com.tech.bee.postservice.annotation.TransactionId;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Aspect
@Component
public class TransactionIdAdvice {

    @Before("@annotation(transactionId)")
    public void before(JoinPoint joinPoint, TransactionId transactionId) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String transactionIdValue = UUID.randomUUID().toString();
        request.setAttribute("transactionId", transactionIdValue);
        MDC.put("traceId", transactionIdValue);
    }
}
