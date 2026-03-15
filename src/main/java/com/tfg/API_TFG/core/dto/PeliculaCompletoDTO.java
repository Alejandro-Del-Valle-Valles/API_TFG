package com.tfg.API_TFG.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Schema(description = "Toda la info básica de la película, más la info de los participantes.")
public class PeliculaCompletoDTO extends PeliculaDTO {
    private List<ParticipanteCompletoDTO> participantes = new ArrayList<>();

    public PeliculaCompletoDTO(UUID id, String descripcion, String nombre, String url, LocalTime duracion,
                               Integer edad, List<ParticipanteCompletoDTO> participantes) {
        super(id, descripcion, nombre, url, duracion, edad);
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
