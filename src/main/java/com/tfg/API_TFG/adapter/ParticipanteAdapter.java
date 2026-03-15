package com.tfg.API_TFG.adapter;

import com.tfg.API_TFG.core.dto.ParticipanteCompletoDTO;
import com.tfg.API_TFG.core.dto.ParticipanteDTO;
import com.tfg.API_TFG.core.entity.Credito;
import com.tfg.API_TFG.core.entity.Participante;
import com.tfg.API_TFG.core.enums.RolParticipante;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ParticipanteAdapter {

    public static ParticipanteDTO toDTO(Participante participante) {
        return new ParticipanteDTO(participante.getId(), participante.getNombre());
    }

    public static ParticipanteCompletoDTO toCompletoDTO(Participante participante, List<RolParticipante> rolesPelicula) {
        return new ParticipanteCompletoDTO(participante.getId(), participante.getNombre(),
                rolesPelicula);
    }
}
