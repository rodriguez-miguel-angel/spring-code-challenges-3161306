package com.cecilireid.springchallenges.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class LoggingAspect {
    Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("within(com.cecilireid.springchallenges.services.*)")
    public void logMethodSignature(JoinPoint jp) {
        logger.info("MethodSignature: {}", jp.getSignature().toString());
    }

    @Before("within(com.cecilireid.springchallenges.controllers.*)")
    public void logRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        logger.info(request.getMethod() + " request made to: " + request.getRequestURI());
    }

    @Around("execution(* com.cecilireid.springchallenges.controllers.*.*ById(Long)) && args(id)")
    public Object logObjects(ProceedingJoinPoint proceedingJoinPoint, Long id) throws Throwable {
        logger.info("Request received to retrieve catering job by id: {}", id);
        Object response = proceedingJoinPoint.proceed();
        logger.info("Response returned for received request: {}", response);
        return response;
    }
}
