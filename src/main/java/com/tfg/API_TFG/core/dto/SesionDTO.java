package com.tfg.API_TFG.core.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Información básica de una sesión")
public record SesionDTO(

        @Schema(
                description = "Número de la sala en la que se proyecta.",
                example = "1"
        )
        @NotNull(message = "El número de la sala no puede ser negativo.")
        @Positive(message = "El número de la sala debe ser positivo.")
        Integer numSala,

        @Schema(
                description = "La sesión se proyecta o no en 3D. True si es 3D, false si no.",
                example = "true"
        )
        @NotNull(message = "No puede ser nulo. True si se proyecta en 3D, false si no.")
        boolean tresD,

        @Schema(
                description = "La sesión se proyecta en VOSE o doblada. True si es VOSE, false si no.",
                example = "false"
        )
        @NotNull(message = "No puede ser nulo. True si es en VOSE, false si no.")
        boolean vose,

        @Schema(
                description = "Información básica de la película"
        )
        @NotNull(message = "La película que se proyecta no puede ser nula.")
        UUID peliculaId,

        @Schema(
                description = "Horario de la película (Día y hora a la que empieza)",
                example = "2026-06-21T18:30",
                type = "string",
                format = "date-time"
        )
        @NotNull(message = "El horario de la película no puede ser nulo.")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime horario
) {
}
