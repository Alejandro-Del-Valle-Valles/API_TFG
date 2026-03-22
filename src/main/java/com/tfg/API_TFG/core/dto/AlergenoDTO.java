package com.tfg.API_TFG.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.Objects;

@Schema(description = "Información de un Alergeno.")
public record AlergenoDTO (

    @Schema(
            description = "Nombre del alérgeno. Ni puede superar los 25 caracteres.",
            example = "Gluten"
    )
    @Length(max = 25, message = "La longitud del nombre no puede ser superior a 25 caracteres.")
    @NotNull(message = "El nombre del alérgeno no puede ser nulo.")
    @NotBlank(message = "El nombre del alérgeno no puede estar vacío.")
    String nombre
) {}
