package com.projeto_pi.services;

import com.projeto_pi.dtos.UsuarioDto;
import com.projeto_pi.models.Usuario;
import com.projeto_pi.repositories.UsuarioRepository;
import com.projeto_pi.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public Usuario selectOne(UUID usuarioId) {

        Usuario usuario = repository
                .findById(usuarioId)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return SecurityUtils.unauthorized(auth, usuario) ? usuario : null;
    }

    @Transactional
    public Usuario update(UUID usuarioId, UsuarioDto dto) {

        Usuario searchedUsuario = repository
                .findById(usuarioId)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (SecurityUtils.unauthorized(auth, searchedUsuario)) {
            Usuario usuario = new Usuario();
            BeanUtils.copyProperties(dto, usuario);
            usuario.setUsuarioId(usuarioId);
            return repository.save(usuario);
        }

        return null;
    }

    @Transactional
    public Boolean delete(UUID usuarioId) throws Exception {
        repository
                .findById(usuarioId)
                .ifPresentOrElse(
                        usuario -> {
                            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                            if (SecurityUtils.unauthorized(auth, usuario)) {
                                repository.deleteById(usuarioId);
                            }
                        },
                        () -> {
                            throw new NoSuchElementException("Usuário não encontrado");
                        });
        return true;
    }
}
