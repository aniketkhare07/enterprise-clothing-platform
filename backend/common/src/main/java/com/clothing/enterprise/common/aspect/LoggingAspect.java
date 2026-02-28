package com.clothing.enterprise.common.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    // Target all public methods in "api" and "service" packages within the enterprise group
    @Around("execution(* com.clothing.enterprise..api..*(..)) || execution(* com.clothing.enterprise..service..*(..))")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        log.info("START: {}.{}", className, methodName);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try {
            Object result = joinPoint.proceed();
            stopWatch.stop();
            log.info("SUCCESS: {}.{} ({} ms)", className, methodName, stopWatch.getTotalTimeMillis());
            return result;
        } catch (Exception e) {
            stopWatch.stop();
            log.error("FAILED: {}.{} ({} ms) - Exception: {}", className, methodName, stopWatch.getTotalTimeMillis(), e.getMessage());
            throw e; // Re-throw so GlobalExceptionHandler catches it
        }
    }
}