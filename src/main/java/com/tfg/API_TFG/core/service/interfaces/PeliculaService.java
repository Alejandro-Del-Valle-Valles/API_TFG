package com.tfg.API_TFG.core.service.interfaces;

import com.tfg.API_TFG.core.dto.PeliculaCompletoAndSesionesDTO;
import com.tfg.API_TFG.core.dto.PeliculaCompletoDTO;
import com.tfg.API_TFG.core.dto.PeliculaCreateDTO;
import com.tfg.API_TFG.core.dto.PeliculaDTO;

import java.util.List;
import java.util.UUID;

public interface PeliculaService {

    List<PeliculaDTO> getAll();
    List<PeliculaCompletoDTO> getAllCompleto();
    List<PeliculaDTO> getByNombreContainingIgonreCase(String nombre);
    List<PeliculaCompletoDTO> getCompletoByNombreContainingIgnoreCase(String nombre);
    PeliculaCompletoDTO getCompletoById(UUID id);
    PeliculaCompletoAndSesionesDTO getCompletoAndSesionesById(UUID id);
    PeliculaCompletoDTO createPelicula(PeliculaCreateDTO peliculaCompletoDTO);
    PeliculaCompletoDTO updatePeliculaCompleto(UUID id, PeliculaCreateDTO peliculaUpdateDTO);
    PeliculaDTO deletePelicula(UUID id);
}
