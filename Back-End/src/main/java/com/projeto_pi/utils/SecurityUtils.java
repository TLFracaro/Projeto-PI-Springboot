package com.projeto_pi.utils;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;

import com.projeto_pi.models.Usuario;

public class SecurityUtils {

    public static boolean unauthorized(Authentication auth, Usuario usuario) {
        if (!auth.getPrincipal().equals(usuario.getEmail()))
            throw new AccessDeniedException("Acesso negado");
        else
            return true;
    }
}
