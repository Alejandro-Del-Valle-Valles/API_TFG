package com.tfg.API_TFG.adapter;

import com.tfg.API_TFG.core.dto.ParticipanteDTO;
import com.tfg.API_TFG.core.entity.Participante;

public class ParticipanteAdapter {

    public static ParticipanteDTO toDTO(Participante participante) {
        return new ParticipanteDTO(participante.getId(), participante.getNombre());
    }
}
