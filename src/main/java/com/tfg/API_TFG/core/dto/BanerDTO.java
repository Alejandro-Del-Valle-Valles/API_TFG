package com.tfg.API_TFG.core.dto;

import com.tfg.API_TFG.core.validation.ValidDateRange;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.UUID;

@ValidDateRange(start = "empieza", end = "termina",
        message = "La fecha de inicio debe ser posterior o igual a la fecha de finalización")
@Schema(description = "Información básica del banner")
public record BanerDTO(

        @Schema(
                description = "ID del banner",
                example = "1"
        )
        Integer id,

        @Schema(
                description = "ID de la película",
                example = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                implementation = UUID.class
        )
        @NotNull(message = "El ID de la película no puede ser nulo")
        UUID peliculaId,

        @Schema(
                description = "URL de la película",
                example = "https://i.pinimg.com/236x/26/3c/f1/263cf1c691187776b2df16e3c9db5366.jpg?nii=t"
        )
        @Length(max = 511, message = "La longitud de la URL no puede ser superior a 511 caracteres")
        @NotNull(message = "La URL no puede ser nula")
        @NotBlank(message = "La URL no puede estar va")
        String url,

        @Schema(
                description = "Fecha en la que empieza a ser visible el baner",
                example = "2026-04-21",
                implementation = LocalDate.class
        )
        @NotNull(message = "La fecha de inicio no puede ser nula")
        LocalDate empieza,

        @Schema(
                description = "Fecha en la que termina de ser visible el baner",
                example = "2026-04-21",
                implementation = LocalDate.class
        )
        @NotNull(message = "La fecha de fin no puede ser nula")
        LocalDate termina
) { }