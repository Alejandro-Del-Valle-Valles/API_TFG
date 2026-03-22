package com.tfg.API_TFG.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@Schema(description = "Contiene el correo de un usuario.")
public record UsuarioDTO (
    @Schema(
            description = "Correo del usuario.",
            example = "ejemplo@gmail.com"
    )
    @NotNull(message = "El correo no puede ser nulo.")
    @NotBlank(message = "El correo no puede estar vacío.")
    @Length(max = 100, message = "El correo no puede contener más de 100 caracteres.")
    String correo
) {}
