package com.tfg.API_TFG.core.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
                description = "Fecha en la que se realiza la compra.",
                example = "2026-06-21T18:30",
                type = "string",
                format = "date-time"
        )
        @NotNull(message = "La fecha de compra no puede ser nula.")
        @PastOrPresent(message = "La fecha no puede ser futura.")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime fecha,

        @ArraySchema(
                schema = @Schema(implementation = LineaCompraDTO.class),
                arraySchema = @Schema(description = "Lista de lineas de compra del DTO")
        )
        @NotNull(message = "La compra no puede tener línea de compras en nulo.")
        @NotEmpty(message = "La compra debe contener al menos una línea de compra.")
        List<LineaCompraDTO> lineasCompra
) {
}
