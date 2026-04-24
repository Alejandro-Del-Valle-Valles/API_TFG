package com.tfg.API_TFG.core.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Estado de las butacas libre y ocupadas")
public record ButacasStatusResponse(

        @ArraySchema(
                schema = @Schema(implementation = ButacaDTO.class),
                arraySchema = @Schema(description = "Lista de las butacas ocupadas")
        )
        List<ButacaDTO> ocupadas,

        @ArraySchema(
                schema = @Schema(implementation = ButacaDTO.class),
                arraySchema = @Schema(description = "Lista de las butacas bloqueadas")
        )
        List<ButacaDTO> bloqueadas
) {
}
