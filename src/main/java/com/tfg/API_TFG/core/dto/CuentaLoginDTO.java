package com.tfg.API_TFG.core.dto;

import com.tfg.API_TFG.core.enums.RolUsuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Infromación de una cuenta tras loguerase.")
public record CuentaLoginDTO(

        @Schema(
                description = "Correo de la cuenta",
                example = "ejemplo@gmail.com"
        )
        @NotNull(message = "El correo no puede ser nulo.")
        @NotBlank(message = "El correo no puede estar vacío.")
        String correo,

        @Schema(
                description = "Nombre del usuario.",
                example = "Antonio"
        )
        @NotNull(message = "El nombre no puede ser nulo.")
        @NotBlank(message = "El nombre no puede estar vacío.")
        String nombre,

        @Schema(
                description = "Rol de la cuenta. Debe ser uno de los roles disponibles",
                example = "CLIENTE",
                allowableValues = {"CLIENTE", "EMPLEADO", "ADMINISTRADOR"},
                implementation = RolUsuario.class
        )
        @NotNull(message = "El rol no puede ser nulo.")
        RolUsuario rol
) {
}
