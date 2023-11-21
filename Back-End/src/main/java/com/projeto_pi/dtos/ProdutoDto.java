package com.projeto_pi.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;


public record ProdutoDto(

        @NotBlank(message = "O campo nome deve ser informado")
        String nome,

        @NotBlank(message = "O campo categoria deve ser informado")
        String categoria,

        @NotBlank(message = "O campo marca deve ser informado")
        String marca,

        @NotNull(message = "O campo preço deve ser informado")
        @Digits(integer = 10, fraction = 2, message = "Valor máximo de 10 dígitos e 2 casas decimais")
        @Positive(message = "O valor deve ser positivo")
        BigDecimal preco,

        @NotBlank(message = "O campo descricao deve ser informado")
        String descricao,

        @NotBlank(message = "O campo iocEstoque deve ser informado")
        String iocEstoque,

        @NotNull(message = "O campo peso deve ser informado")
        @Digits(integer = 10, fraction = 2, message = "Valor máximo de 10 dígitos e 2 casas decimais")
        @Positive(message = "O valor do peso deve ser positivo")
        BigDecimal peso,

        @Valid
        @NotNull(message = "Pelo menos uma variação do produto deve ser informada")
        @NotEmpty(message = "Pelo menos uma variação do produto deve ser informada")
        List<VariacaoDto> variacoes
) {

}
