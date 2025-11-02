package com.example.hospitalsystem.aspect;

import com.example.hospitalsystem.annotation.Auditable;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class AuditAspect {

    @Around("@annotation(auditable)")
    public Object auditMethod(ProceedingJoinPoint joinPoint, Auditable auditable) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String action = auditable.action();

        log.info("📋 AUDIT [{}] - Usuario ejecutando {}.{} con argumentos: {}",
                action, className, methodName, Arrays.toString(joinPoint.getArgs()));

        LocalDateTime startTime = LocalDateTime.now();

        try {
            Object result = joinPoint.proceed();
            log.info("✅ AUDIT [{}] - Operación EXITOSA en {} a las {}",
                    action, className, startTime);
            return result;
        } catch (Exception e) {
            log.error("❌ AUDIT [{}] - ERROR en {}.{}: {}",
                    action, className, methodName, e.getMessage());
            throw e;
        }
    }
}
