package com.projeto_pi.dtos;


import org.hibernate.validator.constraints.br.CPF;

import com.projeto_pi.enums.Privilegio;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern.Flag;

public record UsuarioDto(
    
    @NotBlank(message = "Campo CPF deve ser informado")
    @Size(min = 11, max = 11, message = "O campo CPF deve conter 11 dígitos. Ex: '00011100011'")
    @CPF(message = "Digite um CPF válido")
    String cpf,

    @NotBlank(message = "Campo nome deve ser informado")
    String nome,

    @NotBlank(message = "Campo email deve ser informado")
    @Email(message = "Digite um E-mail válido", flags = {Flag.CASE_INSENSITIVE, Flag.UNICODE_CASE})
    String email,

    @NotBlank(message = "Campo senha deve ser informado")
    String senha,

    @NotNull(message = "Campo privilégio deve ser informado")
    Privilegio privilegio
) {
    
}
