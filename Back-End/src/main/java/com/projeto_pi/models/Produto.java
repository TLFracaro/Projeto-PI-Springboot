package com.projeto_pi.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "produto")
public class Produto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "produto_id")
    private UUID produtoId;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "categoria", nullable = false)
    private String categoria;

    @Column(name = "marca", nullable = false)
    private String marca;

    @Column(name = "preco", nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Column(name = "loc_estoque")
    private String iocEstoque;

    @Column(name = "peso", nullable = false, precision = 10, scale = 2)
    private BigDecimal peso;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Variacao> variacoes = new ArrayList<>();

    @OneToMany(mappedBy = "produto", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, orphanRemoval = true)
    private List<Imagem> imagens = new ArrayList<>();
}
