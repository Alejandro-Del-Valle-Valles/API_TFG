package com.tfg.API_TFG.core.dto;

import com.tfg.API_TFG.core.enums.RolParticipante;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

@Schema(description = "Toda la información de un participante junto a su rol/roles")
public class ParticipanteCompletoDTO extends ParticipanteDTO{

    @ArraySchema(
            schema = @Schema(
                    description = "Lista de roles del participante",
                    example = "[ACTOR, DIRECTOR]",
                    allowableValues = {"ACTOR", "DIRECTOR"},
                    implementation = RolParticipante.class
            ),
            arraySchema = @Schema(description = "Lista de roles")
    )
    private List<RolParticipante> roles = new ArrayList<>();

    public ParticipanteCompletoDTO(Integer id, String nombre, List<RolParticipante> roles) {
        super(id, nombre);
        this.roles = roles;
    }

    public List<RolParticipante> getRoles() {
        return roles;
    }

    public void setRoles(List<RolParticipante> roles) {
        this.roles = roles;
    }

    public void addRol(RolParticipante rol) {
        if(!this.roles.contains(rol)) this.roles.add(rol);
    }
}
