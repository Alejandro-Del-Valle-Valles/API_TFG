package com.tfg.API_TFG.core.service.interfaces;

import com.tfg.API_TFG.core.dto.SesionCrudDTO;
import com.tfg.API_TFG.core.dto.SesionDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface SesionService {
    List<SesionDTO> getAll();
    List<SesionDTO> getAllBetweenHorarios(LocalDateTime horarioInicio, LocalDateTime horarioFin);
    SesionDTO createSesion(SesionCrudDTO sesionCrudDTO);
    SesionDTO updateSesion(SesionCrudDTO antiguaSesion, SesionCrudDTO nuevaSesion);
    SesionDTO deleteSesion(SesionCrudDTO sesionCrudDTO);
}
