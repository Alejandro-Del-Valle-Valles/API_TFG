package com.tfg.API_TFG.core.service.interfaces;

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
    PeliculaCompletoDTO createPelicula(PeliculaCreateDTO peliculaCompletoDTO);
    PeliculaCompletoDTO updatePeliculaCompleto(PeliculaCompletoDTO peliculaCompletoDTO);
    PeliculaDTO deletePelicula(UUID id);
}
