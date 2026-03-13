package com.tfg.API_TFG.adapter;

import com.tfg.API_TFG.core.dto.PeliculaDTO;
import com.tfg.API_TFG.core.entity.Pelicula;

public class PeliculaAdapter {

    public static PeliculaDTO toDTO(Pelicula pelicula) {
        return new PeliculaDTO(pelicula.getId(), pelicula.getDescripcion(), pelicula.getDescripcion(),
                pelicula.getPortada(), pelicula.getDuracion(), pelicula.getCalificacionEdad());
    }
}
