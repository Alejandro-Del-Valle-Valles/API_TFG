package com.tfg.API_TFG.core.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Schema(description = "DTO para la creación de una sesión. Solo incluye las PK.")
public class SesionCreateDTO {

    @Schema(
            description = "Número de la sala en la que se proyecta.",
            example = "1"
    )
    @NotNull(message = "El número de la sala no puede ser negativo.")
    @Positive(message = "El número de la sala debe ser positivo.")
    private Integer numSala;

    @Schema(
            description = "ID de la película que se proyecta"
    )
    @NotNull(message = "La película que se proyecta no puede ser nula.")
    private UUID pelicula_id;

    @Schema(
            description = "Horario de la película (Día y hora a la que empieza)",
            example = "2026-06-21T18:30",
            type = "string",
            format = "date-time"
    )
    @NotNull(message = "El horario de la película no puede ser nulo.")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime horario;

    public SesionCreateDTO(Integer numSala, UUID pelicula_id, LocalDateTime horario) {
        this.numSala = numSala;
        this.pelicula_id = pelicula_id;
        this.horario = horario;
    }

    public Integer getNumSala() { return numSala; }

    public void setNumSala(Integer numSala) { this.numSala = numSala; }

    public UUID getPelicula_id() { return pelicula_id; }

    public void setPelicula_id(UUID pelicula_id) { this.pelicula_id = pelicula_id; }

    public LocalDateTime getHorario() { return horario; }

    public void setHorario(LocalDateTime horario) { this.horario = horario; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SesionCreateDTO that = (SesionCreateDTO) o;
        return Objects.equals(numSala, that.numSala) && Objects.equals(pelicula_id, that.pelicula_id) && Objects.equals(horario, that.horario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numSala, pelicula_id, horario);
    }
}
