package com.projeto_pi.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.projeto_pi.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    @Query("SELECT u FROM Usuario u WHERE u.email = :email OR u.cpf = :cpf")
    Optional<Usuario> findByEmailOrCpf(String email, String cpf);
    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    Optional<Usuario> findByEmail(String email);
}
