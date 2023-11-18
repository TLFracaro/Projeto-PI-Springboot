package com.projeto_pi.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto_pi.models.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, UUID> {
    
}
