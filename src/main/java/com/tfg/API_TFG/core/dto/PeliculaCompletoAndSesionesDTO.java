package com.tfg.API_TFG.core.dto;

import com.tfg.API_TFG.core.enums.GeneroPeliculas;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Schema(description = "Información completa de una película más sus sesiones.")
public class PeliculaCompletoAndSesionesDTO extends PeliculaCompletoDTO{

    @ArraySchema(
            schema = @Schema(implementation = SesionDTO.class),
            arraySchema = @Schema(description = "Lista de sesiones de la película")
    )
    private List<SesionDTO> sesiones = new ArrayList<>();

    public PeliculaCompletoAndSesionesDTO(UUID id, String nombre, String descripcion, GeneroPeliculas genero,
                                          String url, LocalTime duracion, Integer edad, Boolean enCartelera,
                                          List<ParticipanteCompletoDTO> participantes, List<SesionDTO> sesiones) {
        super(id, nombre, descripcion, genero, url, duracion, edad, enCartelera, participantes);
        this.sesiones = sesiones;
    }

    public List<SesionDTO> getSesiones() { return sesiones; }

    public void setSesiones(List<SesionDTO> sesiones) { this.sesiones = sesiones; }
}
