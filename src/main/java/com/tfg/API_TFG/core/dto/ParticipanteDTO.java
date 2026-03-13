package com.tfg.API_TFG.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;

import java.util.Objects;

@Schema(description = "Información de un participante.")
public class ParticipanteDTO {

    @Schema(
            description = "ID del participante. Numero entero.",
            example = "1"
    )
    @Positive(message = "El id no puede ser inferior a 1.")
    @NotNull(message = "El id no puede ser nulo.")
    private Integer id;

    @Schema(
            description = "Nombre del participante.",
            example = "Tom Cruise"
    )
    @Length(max = 50, message = "El nombre no puede ser superior a 50 caracteres.")
    @NotNull(message = "El nombre no puede ser nulo.")
    @NotBlank(message = "El nombre no puede estar vacío.")
    private String nombre;

    public ParticipanteDTO(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ParticipanteDTO that = (ParticipanteDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
