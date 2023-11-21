package com.projeto_pi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

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
