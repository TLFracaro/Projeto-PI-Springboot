package com.projeto_pi.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.projeto_pi.models.Imagem;

public interface ImagemRepository extends JpaRepository<Imagem, UUID> {
    @Modifying
    @Query("DELETE FROM Imagem i WHERE i.produto.produtoId = :produtoId")
    void deleteAllByProdutoId(UUID produtoId);
}
