package com.tfg.API_TFG.core.service.interfaces;

import com.tfg.API_TFG.core.dto.ParticipanteDTO;

import java.util.List;

public interface ParticipanteService {
    List<ParticipanteDTO> getAll();
    List<ParticipanteDTO> getByNombre(String nombre);
    ParticipanteDTO getById(Integer id);
    ParticipanteDTO createParticipante(String nombre);
    ParticipanteDTO updateParticipante(ParticipanteDTO participanteDTO);
    ParticipanteDTO deleteParticipante(Integer id);
}
