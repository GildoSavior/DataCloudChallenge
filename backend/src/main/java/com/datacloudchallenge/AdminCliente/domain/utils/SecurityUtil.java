package com.datacloudchallenge.AdminCliente.domain.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;

public final class SecurityUtil {
    private SecurityUtil() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não pode ser instanciada");
    }

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static String getAuthenticatedPhoneNumber() {
        return getAuthentication().getName();
    }

    public static List<String> getAuthenticatedUserRoles() {
        return getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()); // Retorna todas as roles do usuário autenticado
    }

    public static String getRole() {
        return  getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_CLIENT");
    }
}
