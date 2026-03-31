package com.tfg.API_TFG.core.service.interfaces;

import com.tfg.API_TFG.core.dto.SesionCrudDTO;
import com.tfg.API_TFG.core.dto.SesionCompletaDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface SesionService {
    List<SesionCompletaDTO> getAll();
    List<SesionCompletaDTO> getAllBetweenHorarios(LocalDateTime horarioInicio, LocalDateTime horarioFin);
    SesionCompletaDTO getSesion(Integer numSala, UUID peliculaId, LocalDateTime horario);
    SesionCompletaDTO createSesion(SesionCrudDTO sesionCrudDTO);
    SesionCompletaDTO deleteSesion(SesionCrudDTO sesionCrudDTO);
}
