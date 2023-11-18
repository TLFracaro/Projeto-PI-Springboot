package com.projeto_pi.models;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "variacao")
@EqualsAndHashCode(of = {"tamanho", "cor", "quantidade"})
public class Variacao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "variacao_id")
    private UUID variacaoId;

    @Column(name = "tamanho", nullable = false)
    private String tamanho;

    @Column(name = "cor", nullable = false)
    private String cor;

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    @JsonIgnore
    private Produto produto;
}
