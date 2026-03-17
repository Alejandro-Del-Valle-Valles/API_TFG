package com.tfg.API_TFG.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Objects;
import java.util.UUID;

@Schema(description = "Información básica de una Pleícula.")
public class PeliculaDTO {

    @Schema(
            description = "ID de la Película",
            example = "d3ee2929-212b-4077-af84-694a0e69b8e1"
    )
    @NotNull(message = "El ID de la película no puede ser nulo.")
    private UUID id;

    @Schema(
            description = "Descripción de la película.",
            example = "Descubre la historia del Titanic desde la perspectiva de un joven chico interpretado por Leonardo Dicaprio..."
    )
    @Length(max = 511, message = "La descripción no puede tener más de 511 caracteres.")
    private String descripcion;

    @Schema(
            description = "Nombre de la película",
            example = "Titanic"
    )
    @NotNull(message = "El nombre de la película no puede ser nulo.")
    @NotBlank(message = "El nombre de la película no puede estar en blanco.")
    @Length(max = 50, message = "El nombre no puede contener más de 50 caracteres.")
    private String nombre;

    @Schema(
            description = "URL de la magen de la portada de la película",
            example = "https://es.web.img3.acsta.net/medias/nmedia/18/86/91/41/19870073.jpg"
    )
    @Length(max = 511, message = "El enlace no puede ser superior a 50 caracteres.")
    private String url;

    @Schema(
            description = "Duración en horas y minutos "
    )
    @NotNull(message = "La duración no puede ser nula.")
    private LocalTime duracion;

    @PositiveOrZero(message = "La calificación de la película debe ser positiva.")
    private Integer edad;

    @Schema(
            description = "Null si no está en cartelera, true si está en cartelera, false si es de estreno.",
            example = "true"
    )
    private Boolean enCartelera;

    public PeliculaDTO(UUID id, String descripcion, String nombre, String url, LocalTime duracion, Integer edad) {
        this.id = id;
        this.descripcion = descripcion;
        this.nombre = nombre;
        this.url = url;
        this.duracion = duracion;
        this.edad = edad;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalTime getDuracion() {
        return duracion;
    }

    public void setDuracion(LocalTime duracion) {
        this.duracion = duracion;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PeliculaDTO that = (PeliculaDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
