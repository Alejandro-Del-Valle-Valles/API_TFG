package com.tfg.API_TFG.adapter;

import com.tfg.API_TFG.core.dto.ParticipanteCompletoDTO;
import com.tfg.API_TFG.core.dto.PeliculaCompletoDTO;
import com.tfg.API_TFG.core.dto.PeliculaDTO;
import com.tfg.API_TFG.core.entity.Credito;
import com.tfg.API_TFG.core.entity.Participante;
import com.tfg.API_TFG.core.entity.Pelicula;
import com.tfg.API_TFG.core.enums.RolParticipante;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PeliculaAdapter {

    public static PeliculaDTO toDTO(Pelicula pelicula) {
        return new PeliculaDTO(pelicula.getId(), pelicula.getDescripcion(), pelicula.getNombre(),
                pelicula.getPortada(), pelicula.getDuracion(), pelicula.getCalificacionEdad());
    }

    public static PeliculaCompletoDTO toCompletoDTO(Pelicula pelicula) {
        Map<Participante, List<RolParticipante>> participanteRoles = new HashMap<>();
        for (Credito credito : pelicula.getCreditos()) {
            Participante participante = credito.getParticipante();
            participanteRoles
                    .computeIfAbsent(participante, k -> new ArrayList<>())
                    .add(credito.getRol());
        }
        List<ParticipanteCompletoDTO> participantes = participanteRoles.entrySet().stream()
                .map(entry -> ParticipanteAdapter.toCompletoDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        return new PeliculaCompletoDTO(pelicula.getId(), pelicula.getDescripcion(), pelicula.getNombre(),
                pelicula.getPortada(), pelicula.getDuracion(), pelicula.getCalificacionEdad(), participantes);
    }
}
