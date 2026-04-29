package com.tfg.API_TFG.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
        @NotNull(message = "El numero de la fila no puede ser nulo.")
        @Positive(message = "El numero de la fila no puede ser inferior a 1.")
        Integer numFila,

        @Schema(
                description = "Número de la butaca",
                example = "2"
        )
        @NotNull(message = "El numero de la butaca no puede ser nulo.")
        @Positive(message = "El numero de butaca no puede ser inferior a 1.")
        Integer numButaca,

        @Schema(
                description = "Tipo de entrada.",
                implementation = TipoEntradaDTO.class
        )
        TipoEntradaDTO tipoEntrada
) {
}
