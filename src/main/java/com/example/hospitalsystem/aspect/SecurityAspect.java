package com.example.hospitalsystem.aspect;

import com.example.hospitalsystem.annotation.RequiresRole;
import com.example.hospitalsystem.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class SecurityAspect {

    @Before("@annotation(requiresRole)")
    public void checkAuthorization(JoinPoint joinPoint, RequiresRole requiresRole) {
        String requiredRole = requiresRole.value();
        String methodName = joinPoint.getSignature().getName();

        // AQUÍ conectas con tu sistema de autenticación real
        // Por ahora simulo la verificación
        String currentUserRole = getCurrentUserRole();

        log.info("🔒 Verificando autorización: método={}, rol requerido={}, rol actual={}",
                methodName, requiredRole, currentUserRole);

        if (!hasPermission(currentUserRole, requiredRole)) {
            log.error("⛔ Acceso denegado para {} al método {}", currentUserRole, methodName);
            throw new UnauthorizedException(
                    "No tienes permiso para ejecutar esta operación. Se requiere rol: " + requiredRole
            );
        }

        log.info("✅ Autorización concedida para {}", methodName);
    }

    // Método temporal - REEMPLAZAR con tu lógica de autenticación
    private String getCurrentUserRole() {
        // TODO: Obtener del SecurityContext o JWT
        return "DOCTOR"; // Simulación
    }

    private boolean hasPermission(String userRole, String requiredRole) {
        // TODO: Implementar lógica de roles real
        return userRole.equals(requiredRole) || userRole.equals("ADMIN");
    }
}