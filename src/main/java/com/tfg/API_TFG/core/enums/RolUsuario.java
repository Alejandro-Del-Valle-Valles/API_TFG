package com.tfg.API_TFG.core.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Roles dipsonibles para los usuarios de la app",
        enumAsRef = true
)
public enum RolUsuario {
    @Schema(description = "Cliente básico de la APP")
    CLIENTE,
    @Schema(description = "Empleado del cine")
    EMPLEADO,
    @Schema(description = "Administrador del cine")
    ADMINISTRADOR
}
