package com.tfg.API_TFG.adapter;

import com.tfg.API_TFG.core.dto.SesionCompletaDTO;
import com.tfg.API_TFG.core.dto.SesionDTO;
import com.tfg.API_TFG.core.entity.Sesion;

public class SesionAdapter {

    public static SesionDTO toDTO(Sesion sesion) {
        return new SesionDTO(sesion.getSala().getNumSala(), sesion.isTresD(), sesion.isVose(),
                sesion.getPelicula().getId(), sesion.getHorario());
    }

    public static SesionCompletaDTO toCompletoDTO(Sesion sesion) {
        return new SesionCompletaDTO(sesion.getSala().getNumSala(), sesion.isTresD(), sesion.isVose(),
                PeliculaAdapter.toDTO(sesion.getPelicula()), sesion.getHorario());
    }
}
