package com.tfg.API_TFG.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;

@Schema(description = "Información para crear un nuevo tipo de entrada")
public record CreateTipoEntradaDTO(
        @Schema(
                description = "Nombre de la entrada",
                example = "Adulto"
        )
        @NotNull(message = "El nombre del producto no puede ser nulo.")
        @NotBlank(message = "El nombre del producto no puede estar vacío.")
        @Length(max = 20, message = "El nombre no puede contener más de 20 caracteres.")
        String nombre,

        @Schema(
                description = "Descripción breve de la entrada",
                example = "A partir de 13 años"
        )
        @NotNull(message = "El nombre del producto no puede ser nulo.")
        @NotBlank(message = "El nombre del producto no puede estar vacío.")
        @Length(max = 50, message = "El nombre no puede contener más de 50 caracteres.")
        String descripcion,

        @Schema(
                description = "Precio de la entrada",
                example = "8.50",
                implementation = Float.class
        )
        @NotNull(message = "El precio no puede ser nulo.")
        @Positive(message = "El precio no puede ser 0 o negativo.")
        Float precio
) {
}
