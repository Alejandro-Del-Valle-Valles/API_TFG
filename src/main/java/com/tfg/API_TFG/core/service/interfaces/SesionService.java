package com.tfg.API_TFG.core.service.interfaces;

import com.tfg.API_TFG.core.dto.SesionCrudDTO;
import com.tfg.API_TFG.core.dto.SesionCompletaDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface SesionService {
    List<SesionCompletaDTO> getAll();
    List<SesionCompletaDTO> getAllBetweenHorarios(LocalDateTime horarioInicio, LocalDateTime horarioFin);
    SesionCompletaDTO getSesion(SesionCrudDTO sesionCrudDTO);
    SesionCompletaDTO createSesion(SesionCrudDTO sesionCrudDTO);
    SesionCompletaDTO updateSesion(SesionCrudDTO antiguaSesion, SesionCrudDTO nuevaSesion);
    SesionCompletaDTO deleteSesion(SesionCrudDTO sesionCrudDTO);
}
