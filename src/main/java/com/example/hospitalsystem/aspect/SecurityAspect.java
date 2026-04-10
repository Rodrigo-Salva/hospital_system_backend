package com.example.hospitalsystem.aspect;

import com.example.hospitalsystem.annotation.RequiresRole;
import com.example.hospitalsystem.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Aspect
@Component
@Slf4j
public class SecurityAspect {

    @Before("@annotation(requiresRole)")
    public void checkAuthorization(JoinPoint joinPoint, RequiresRole requiresRole) {
        String requiredRole = requiresRole.value();
        String methodName = joinPoint.getSignature().getName();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            log.error("Acceso denegado (no autenticado) al metodo {}", methodName);
            throw new UnauthorizedException("Debes autenticarte para ejecutar esta operacion");
        }

        String username = authentication.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        boolean hasRole = authorities.stream()
                .anyMatch(a -> a.getAuthority().equals(requiredRole)
                        || a.getAuthority().equals("ROLE_ADMIN"));

        log.info("Verificando autorizacion: metodo={}, rol requerido={}, usuario={}",
                methodName, requiredRole, username);

        if (!hasRole) {
            log.error("Acceso denegado para usuario={} al metodo {}", username, methodName);
            throw new UnauthorizedException(
                    "No tienes permiso para ejecutar esta operacion. Se requiere rol: " + requiredRole
            );
        }

        log.info("Autorizacion concedida para usuario={} en metodo {}", username, methodName);
    }
}
