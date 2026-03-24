package com.tfg.API_TFG.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

@Schema(description = "Información de una entrada")
public record EntradaDTO(

        @Schema(
                description = "Sesión de la entrada",
                implementation = SesionDTO.class
        )
        @NotNull(message = "La sesión no puede ser nula.")
        SesionDTO sesion,

        @Schema(
                description = "Número de la fila",
                example = "5"
        )
        Integer numFila,

        @Schema(
                description = "Número de la butaca",
                example = "2"
        )
        Integer numButaca,

        @Schema(
                description = "Precio de la entrada.",
                example = "7.50"
        )
        @NotNull(message = "El precio de la entrada no puede ser nulo.")
        @PositiveOrZero(message = "El precio de la entrada no puede ser negativo.")
        BigDecimal precio
) {
}
