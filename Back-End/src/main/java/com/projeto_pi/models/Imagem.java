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
@Table(name = "imagem")
@EqualsAndHashCode(of = {"url"})
public class Imagem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "imagem_id")
    private UUID imagemId;

    @Column(name = "url", nullable = false)
    private String url;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    @JsonIgnore
    private Produto produto;
}
