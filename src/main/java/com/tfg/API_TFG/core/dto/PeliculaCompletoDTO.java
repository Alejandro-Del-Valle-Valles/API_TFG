package com.tfg.API_TFG.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Schema(description = "Toda la info básica de la película, más la info de los participantes.")
public class PeliculaCompletoDTO extends PeliculaDTO{

    //TODO: Pensar si hacer un DTO específico para los participantes
    private List<String> directores = new ArrayList<>();
    private List<String> actores = new ArrayList<>();

    public PeliculaCompletoDTO(UUID id, String descripcion, String nombre, String url, LocalTime duracion, Integer edad,
                               List<String> directores, List<String> actores) {
        super(id, descripcion, nombre, url, duracion, edad);
        this.directores = directores;
        this.actores = actores;
    }

    //TODO: No implemento nada más hasta pensar como hacerlo bien
}
