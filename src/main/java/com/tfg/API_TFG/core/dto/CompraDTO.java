package com.tfg.API_TFG.core.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Schema(description = "Información de una compra.")
public record CompraDTO(

        @Schema(
                description = "Correo del usuario",
                example = "ejemplo@gmail.com"
        )
        @NotBlank(message = "El correo del usuario no puede estar vacío.")
        @NotNull(message = "El correo del usuario no puede ser nulo.")
        @Length(max = 100, message = "El correo no puede contener más de 100 caracteres.")
        String correo,

        @Schema(
                description = "Token de reserva de butacas.",
                example = "TokenDeReservDeButacasEjemplo"
        )
        @NotBlank(message = "El token no puede estar vacío.")
        @NotNull(message = "El Token no puede ser nulo.")
        String holdToken,

        @ArraySchema(
                schema = @Schema(implementation = LineaCompraDTO.class),
                arraySchema = @Schema(description = "Lista de lineas de compra del DTO")
        )
        @NotNull(message = "La compra no puede tener línea de compras en nulo.")
        @NotEmpty(message = "La compra debe contener al menos una línea de compra.")
        List<LineaCompraDTO> lineasCompra
) {
}
