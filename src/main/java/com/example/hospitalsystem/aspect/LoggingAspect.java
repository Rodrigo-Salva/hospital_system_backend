package com.example.hospitalsystem.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Before("execution(* com.example.hospitalsystem.controller.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        log.info("==> REQUEST: {} con parámetros: {}",
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "execution(* com.example.hospitalsystem.controller.*.*(..))",
            returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("<== RESPONSE: {} devolvió: {}",
                joinPoint.getSignature().getName(), result);
    }

    @AfterThrowing(pointcut = "execution(* com.example.hospitalsystem.controller.*.*(..))",
            throwing = "error")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
        log.error("XXX EXCEPTION en {}: {}",
                joinPoint.getSignature().getName(), error.getMessage());
    }
}
