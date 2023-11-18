package com.projeto_pi.dtos;

import com.projeto_pi.utils.ValidateBase64;
import com.projeto_pi.utils.ValidateExtension;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImagemDto {
    
    @NotBlank(message = "O conteúdo da imagem deve ser informado")
    @ValidateBase64
    private String base64;

    @NotBlank(message = "A extensão da imagem deve ser informado")
    @ValidateExtension
    private String extension;
}
