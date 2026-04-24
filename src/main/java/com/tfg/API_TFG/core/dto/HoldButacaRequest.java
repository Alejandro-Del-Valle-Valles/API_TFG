package com.tfg.API_TFG.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Petición para bloquear una butaca")
public record HoldButacaRequest(

        @Schema(
                description = "Token dado para poder bloquear butacas",
                example = "TokenParaPOderBloquearButacas"
        )
        @NotNull(message = "El token no puede ser nulo.")
        @NotEmpty(message = "El token no puede estra vacío")
        String token,

        @Schema(
                description = "Número de la fila de la butaca a bloquear.",
                example = "7"
        )
        @NotNull(message = "El número de fila no puede ser nulo.")
        @Positive(message = "El número debe ser positivo.")
        Integer fila,

        @Schema(
                description = "Número de la butaca en la fila a bloquear.",
                example = "4"
        )
        @NotNull(message = "El número de butaca no puede ser nulo.")
        @Positive(message = "El número debe ser positivo.")
        Integer butaca
) {
}
