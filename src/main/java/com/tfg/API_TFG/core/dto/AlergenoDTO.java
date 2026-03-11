package com.tfg.API_TFG.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.Objects;

@Schema(description = "Datos para CRUD de un Alérgeno.")
public class AlergenoDTO {

    @Schema(
            description = "Nombre del alérgeno. Ni puede superar los 25 caracteres.",
            example = "Gluten"
    )
    @Length(max = 25, message = "La longitud del nombre no puede ser superior a 25 caracteres.")
    @NotNull(message = "El nombre del alérgeno no puede ser nulo.")
    @NotBlank(message = "El nombre del alérgeno no puede estar vacío.")
    private String nombre;

    public AlergenoDTO(String nombre) {
        this.nombre = nombre;
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
        AlergenoDTO that = (AlergenoDTO) o;
        return Objects.equals(nombre, that.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nombre);
    }
}
