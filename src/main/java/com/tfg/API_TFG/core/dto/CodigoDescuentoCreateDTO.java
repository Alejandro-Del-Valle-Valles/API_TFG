package com.tfg.API_TFG.core.dto;

import com.tfg.API_TFG.core.enums.CondicionDescuento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@Schema(description = "DTO para la creación y actualización de códigos de descuento.")
public record CodigoDescuentoCreateDTO(

        @Schema(
                description = "Código que introducirá el usuario para aplicar el descuento",
                example = "VERANO2025"
        )
        @NotNull(message = "El código no puede ser nulo.")
        @NotBlank(message = "El código no puede estar vacío.")
        @Length(max = 25, message = "El código no puede contener más de 25 caracteres.")
        String codigo,

        @Schema(
                description = "Condición de aplicación del descuento",
                example = "PELICULA",
                allowableValues = { "COMIDA", "PELICULA", "TODOS" },
                implementation = CondicionDescuento.class
        )
        @NotNull(message = "Debe especificarse una condición.")
        CondicionDescuento condicion,

        @Schema(
                description = "Porcentaje de descuento a aplicar",
                example = "15"
        )
        @NotNull(message = "El porcentaje de descuento no puede ser nulo.")
        @Min(value = 1, message = "El porcentaje mínimo es 1.")
        @Max(value = 100, message = "El porcentaje máximo es 100.")
        Integer porcentajeDescuento,

        @Schema(
                description = "Indica si el código está activo y puede utilizarse",
                example = "true"
        )
        @NotNull(message = "Debe indicarse si el código está activo.")
        Boolean activo

) {}
