package com.tfg.API_TFG.core.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Rol de los participantes de una película",
        enumAsRef = true
)
public enum RolParticipante {
    @Schema(description = "Actor/Actriz de la película")
    ACTOR,
    @Schema(description = "Director/a de la película")
    DIRECTOR
}
