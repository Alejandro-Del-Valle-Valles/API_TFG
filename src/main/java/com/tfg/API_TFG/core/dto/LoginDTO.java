package com.tfg.API_TFG.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@Schema(description = "Correo y contraseña para hacer login")
public record LoginDTO(
        @Schema(
                description = "Correo de la cuenta.",
                example = "ejemplo@gmail.com"
        )
        @NotNull(message = "El correo no puede ser nulo.")
        @NotBlank(message = "El correo no puede estar vacío.")
        @Length(max = 100, message = "El correo no puede contener más de 100 caracteres.")
        String correo,

        @Schema(
                description = "Contraseña de la cuenta.",
                example = "Abril2026"
        )
        @NotNull(message = "La contraseña no puede ser nula.")
        @NotBlank(message = "La contraseña no puede estar vacía.")
        String contrasena
) {
}
