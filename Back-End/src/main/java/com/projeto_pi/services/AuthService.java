package com.projeto_pi.services;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.projeto_pi.dtos.AuthDto;
import com.projeto_pi.dtos.UsuarioDto;
import com.projeto_pi.models.Usuario;
import com.projeto_pi.repositories.UsuarioRepository;
import com.projeto_pi.security.TokenService;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private AuthenticationConfiguration configuration;

    @Autowired
    private TokenService tokenService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repository.findByEmail(email).get();
    }

    public String login(AuthDto dto) throws Exception {
        return tokenService
                .generateToken((Usuario) configuration.getAuthenticationManager()
                        .authenticate(new UsernamePasswordAuthenticationToken(dto.email(), dto.senha()))
                        .getPrincipal());
    }

    public String registrar(UsuarioDto dto) throws Exception {
        if (repository.findByEmailOrCpf(dto.email(), dto.cpf()).isPresent()) {
            throw new IllegalArgumentException("Usuário já cadastrado");
        }
        Usuario usuario = new Usuario();
        BeanUtils.copyProperties(dto, usuario);
        usuario.setSenha(new BCryptPasswordEncoder().encode(dto.senha()));

        usuario = Optional.ofNullable(repository.save(usuario)).orElseThrow(() -> {
            throw new RuntimeException("Não foi possivel efetuar o cadastro, tente novamente");
        });

        return login(new AuthDto(usuario.getEmail(), dto.senha()));
    }
}