package com.tfg.API_TFG.core.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.validator.constraints.Length;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "DTO para la creación y actualización de info básica de películas.")
public record PeliculaCreateDTO (

    @Schema(
            description = "Descripción de la película.",
            example = "Descubre la historia del Titanic desde la perspectiva de un joven chico interpretado por Leonardo Dicaprio..."
    )
    @Length(max = 511, message = "La descripción no puede tener más de 511 caracteres.")
    String descripcion,

    @Schema(
            description = "Nombre de la película",
            example = "Titanic"
    )
    @NotNull(message = "El nombre de la película no puede ser nulo.")
    @NotBlank(message = "El nombre de la película no puede estar en blanco.")
    @Length(max = 50, message = "El nombre no puede contener más de 50 caracteres.")
    String nombre,

    @Schema(
            description = "URL de la magen de la portada de la película",
            example = "https://es.web.img3.acsta.net/medias/nmedia/18/86/91/41/19870073.jpg"
    )
    @Length(max = 511, message = "El enlace no puede ser superior a 50 caracteres.")
    String url,

    @Schema(
            description = "Duración en horas y minutos ",
            example = "03:49"
    )
    @NotNull(message = "La duración no puede ser nula.")
    LocalTime duracion,

    @Schema(
            description = "La calificación de edad de la película. (Edad minima recomendada)",
            example = "3"
    )
    @PositiveOrZero(message = "La calificación de la película debe ser positiva.")
    Integer edad,

    @Schema(
            description = "Null si no está en cartelera, true si está en cartelera, false si es de estreno.",
            example = "true"
    )
    Boolean enCartelera,

    @ArraySchema(
            schema = @Schema(implementation = ParticipanteCompletoDTO.class),
            arraySchema = @Schema(description = "Lista de participantes que actúan en la película")
    )
    List<ParticipanteCompletoDTO> participantes
) {}
