package com.projeto_pi.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AuthDto(
    
    @NotBlank(message = "Email deve ser informado")
    @Email(message = "Email inválido")
    String email,

    @NotBlank(message = "Uma senha deve ser informada")
    @Pattern(regexp = "^[\\p{L}\\p{N} ]+$", message = "O campo deve conter apenas letras, números e espaços, sem acentos.")
    String senha
) {
    
}