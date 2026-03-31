package com.tfg.API_TFG.core.dto;

import com.tfg.API_TFG.core.enums.RolParticipante;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

@Schema(description = "Información básica de un participante en una película")
public record ParticipanteCreateDTO(
        @Schema(
                description = "ID del participante",
                example = "1"
        )
        @NotNull(message = "El ID del participante no puede ser nulo.")
        @Positive(message = "El ID del participante debe ser positivo.")
        Integer id,

        @ArraySchema(
                schema = @Schema(
                        description = "Lista de roles del participante",
                        example = """
                                "ACTOR", "DIRECTOR"
                                """,
                        allowableValues = {"ACTOR", "DIRECTOR"},
                        implementation = RolParticipante.class
                ),
                arraySchema = @Schema(description = "Lista de roles")
        )
        @NotNull(message = "El participante debe tener algún rol.")
        @NotEmpty(message = "La lista de roles no puede estar vacía. Debe tener al menos 1 rol.")
        List<RolParticipante> roles
) {
}
