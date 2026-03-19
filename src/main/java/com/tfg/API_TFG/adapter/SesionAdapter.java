package com.tfg.API_TFG.adapter;

import com.tfg.API_TFG.core.dto.SesionDTO;
import com.tfg.API_TFG.core.entity.Sesion;

public class SesionAdapter {

    public static SesionDTO toDTO(Sesion sesion) {
        return new SesionDTO(sesion.getSala().getNumSala(),
                PeliculaAdapter.toDTO(sesion.getPelicula()), sesion.getHorario());
    }
}
