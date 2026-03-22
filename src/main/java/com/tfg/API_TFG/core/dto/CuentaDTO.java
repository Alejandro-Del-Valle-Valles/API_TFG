package com.tfg.API_TFG.core.dto;

import com.tfg.API_TFG.core.enums.RolUsuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@Schema(description = "Información de la cuenta y el usuario al que pertenece")
public record CuentaDTO(

        @Schema(
                description = "Correo del usuario",
                example = "ejemplo@gmail.com"
        )
        @NotNull(message = "El correo no puede ser nulo.")
        @NotBlank(message = "El correo no puede estar en blanco.")
        @Length(max = 100, message = "El correo no puede tener más de 100 caracteres.")
        String correo,

        @Schema(
                description = "Nombre del usuario",
                example = "Blahaj"
        )
        @NotNull(message = "El nombre no puede ser nulo.")
        @NotBlank(message = "El nombre no puede estar en blanco.")
        @Length(max = 50, message = "El nombre no puede tener más de 50 caracteres.")
        String nombre,

        @Schema(
                description = "Contraseña previamente cifrada",
                example = "0Z8VgSH7gjOULiwhxjObs9W5ynS82tQ4xQRVO0LEoGqMhTGsha5S8AzuZsbFEehgh2jLmC0qJnd+3pU7fA4Bkg"
        )
        @NotNull(message = "La contraseña no puede ser nula.")
        @NotBlank(message = "La contraseña no puede estar vacía.")
        String contrasenya,

        @Schema(
                description = "Rol de la cuenta. Debe ser uno de los roles disponibles",
                example = "CLIENTE",
                allowableValues = {"CLIENTE", "EMPLEADO", "ADMINISTRADOR"},
                implementation = RolUsuario.class
        )
        @NotNull(message = "El rol no puede ser nulo.")
        RolUsuario rol
) { }
