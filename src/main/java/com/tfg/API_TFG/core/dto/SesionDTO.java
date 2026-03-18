package com.tfg.API_TFG.core.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.Objects;

@Schema(description = "DTO de sesión que contiene la el numero de la sala, la info básica de la película y el horario en el que se proyecta.")
public class SesionDTO {

    @Schema(
            description = "Número de la sala en la que se proyecta.",
            example = "1"
    )
    @NotNull(message = "El número de la sala no puede ser negativo.")
    @Positive(message = "El número de la sala debe ser positivo.")
    private Integer numSala;

    @Schema(
            description = "Información básica de la película"
    )
    @NotNull(message = "La película que se proyecta no puede ser nula.")
    private @Valid PeliculaDTO pelicula;

    @Schema(
            description = "Horario de la película (Día y hora a la que empieza)",
            example = "2026-06-21T18:30",
            type = "string",
            format = "date-time"
    )
    @NotNull(message = "El horario de la película no puede ser nulo.")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime horario;

    public SesionDTO(Integer numSala, PeliculaDTO pelicula, LocalDateTime horario) {
        this.numSala = numSala;
        this.pelicula = pelicula;
        this.horario = horario;
    }

    public Integer getNumSala() { return numSala; }

    public void setNumSala(Integer numSala) { this.numSala = numSala; }

    public PeliculaDTO getPelicula() { return pelicula; }

    public void setPelicula(PeliculaDTO pelicula) { this.pelicula = pelicula; }

    public LocalDateTime getHorario() { return horario; }

    public void setHorario(LocalDateTime horario) { this.horario = horario; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SesionDTO sesionDTO = (SesionDTO) o;
        return Objects.equals(numSala, sesionDTO.numSala) && Objects.equals(pelicula, sesionDTO.pelicula) && Objects.equals(horario, sesionDTO.horario);
    }

    @Override
    public int hashCode() { return Objects.hash(numSala, pelicula, horario); }
}
