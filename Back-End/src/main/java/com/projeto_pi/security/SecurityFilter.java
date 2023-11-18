package com.projeto_pi.security;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.projeto_pi.repositories.UsuarioRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService service;

    @Autowired
    private UsuarioRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = recoverToken(request);

        if (token != null) {
            repository
                    .findByEmail(service.validateToken(token))
                    .ifPresentOrElse(usuario -> {
                        SecurityContextHolder
                                .getContext()
                                .setAuthentication(new UsernamePasswordAuthenticationToken(
                                        usuario.getEmail(),
                                        usuario.getPassword(),
                                        usuario.getAuthorities()));
                    }, () -> {
                        throw new NoSuchElementException("Usuário não encontrado");
                    });
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return token != null
                ? token.substring(7)
                : null;
    }
}
