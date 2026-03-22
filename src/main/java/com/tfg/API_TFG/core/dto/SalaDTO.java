package com.tfg.API_TFG.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Infromación de una sala.")
public record SalaDTO (

    @Schema(
            description = "Número de la sala.",
            examples = "1"
    )
    @Positive(message = "El número de la sala no puede ser inferior a 1.")
    @NotNull(message = "La sala debe tener un número, no puede ser nulo.")
    Integer numero,

    @Schema(
            description = "Aforo de la sala.",
            examples = "100"
    )
    @Positive(message = "El aforo de la sala no pude ser inferior a 1.")
    @NotNull(message = "El aforo de la sala no puede ser nulo.")
    Integer aforo
) {}
