package com.tfg.API_TFG.core.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Schema(description = "Respuesta del token para seleccionar y tiempo de expiración")
public record HoldTokenResponse(

        @Schema(
                description = "Token para la selección",
                example = "tokenDeEjemploParaLaSelección"
        )
        @NotNull(message = "El token no puede ser nulo.")
        @NotEmpty(message = "El token no puede estar vacío.")
        String token,

        @Schema(
                description = "Fehca en la que expira el token",
                example = "2026-04-21T18:30:00",
                type = "string",
                format = "date-time"
        )
        @NotNull(message = "EL tiempo no puede ser nulo.")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime expira
) {
}
