package com.tfg.API_TFG.core.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Schema(description = "Información de un producto.")
public record ProductoDTO (

    @Schema(
            description = "Nombre del producto. Es único por cada producto.",
            example = "Palomitas"
    )
    @NotNull(message = "El nombre del producto no puede ser nulo.")
    @NotBlank(message = "El nombre del producto no puede estar vacío.")
    @Length(max = 50, message = "El nombre del producto no puede contener más de 50 caracteres.")
    String nombre,

    @Schema(
            description = "Precio del producto en euros. Máximo 5 dígitios de los cuales, 2 son decimales.",
            example = "3.50"
    )
    @NotNull(message = "El precio del producto no puede ser nulo.")
    @PositiveOrZero(message = "El precio del producto no puede ser negativo.")
    BigDecimal precio,

    @Schema(
            description = "Stock disponible.",
            example = "20"
    )
    @NotNull(message = "EL stock no puede ser nulo.")
    @PositiveOrZero(message = "El stock no puede ser negativo.")
    Integer stock,

    @ArraySchema(
            schema = @Schema(implementation = AlergenoDTO.class),
            arraySchema = @Schema(description = "Lista de alérgenos del producto")
    )
    List<@Valid AlergenoDTO> alergenos
) {}
