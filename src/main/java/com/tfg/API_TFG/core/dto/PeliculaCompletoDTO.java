package com.tfg.API_TFG.core.dto;

import com.tfg.API_TFG.core.enums.GeneroPeliculas;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Schema(description = "Toda la info básica de la película, más la info de los participantes.")
public class PeliculaCompletoDTO extends PeliculaDTO {

    @ArraySchema(
            schema = @Schema(implementation = ParticipanteCompletoDTO.class),
            arraySchema = @Schema(description = "Lista de participantes que actúan en la película")
    )
    private List<ParticipanteCompletoDTO> participantes = new ArrayList<>();

    public PeliculaCompletoDTO(UUID id, String nombre, String descripcion, GeneroPeliculas genero, String url, LocalTime duracion,
                               Integer edad, Boolean enCartelera, List<ParticipanteCompletoDTO> participantes) {
        super(id, nombre, descripcion, genero, url, duracion, edad, enCartelera);
        this.participantes = participantes;
    }

    public List<ParticipanteCompletoDTO> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(List<ParticipanteCompletoDTO> participantes) {
        this.participantes = participantes;
    }

    public void addParticipante(ParticipanteCompletoDTO participanteCompletoDTO) {
        if(!this.participantes.contains(participanteCompletoDTO))
            this.participantes.add(participanteCompletoDTO);
    }
}
