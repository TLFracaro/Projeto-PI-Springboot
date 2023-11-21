package com.projeto_pi.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record VariacaoDto(

        @NotBlank(message = "O campo tamanho deve ser informado")
        String tamanho,

        @NotBlank(message = "O campo cor deve ser informado")
        String cor,

        @NotNull(message = "O campo quantidade deve ser informado")
        @PositiveOrZero(message = "O valor da quantidade deve ser positivo")
        @Max(value = Integer.MAX_VALUE, message = "O valor máximo foi atingido")
        Integer quantidade
) {

}
