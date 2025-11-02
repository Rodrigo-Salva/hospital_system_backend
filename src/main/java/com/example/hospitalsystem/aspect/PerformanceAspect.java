package com.example.hospitalsystem.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class PerformanceAspect {

    @Around("@annotation(com.example.hospitalsystem.annotation.MonitorPerformance)")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().toShortString();

        log.info("⏱️ Iniciando medición de: {}", methodName);

        Object result = joinPoint.proceed();

        long duration = System.currentTimeMillis() - startTime;

        if (duration > 5000) {
            log.warn("⚠️ OPERACIÓN LENTA: {} tomó {}ms", methodName, duration);
        } else {
            log.info("✓ {} completado en {}ms", methodName, duration);
        }

        return result;
    }
}

