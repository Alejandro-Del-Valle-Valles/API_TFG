package com.tfg.API_TFG.core.service;

import com.tfg.API_TFG.adapter.SesionAdapter;
import com.tfg.API_TFG.core.dto.SesionCrudDTO;
import com.tfg.API_TFG.core.dto.SesionCompletaDTO;
import com.tfg.API_TFG.core.entity.*;
import com.tfg.API_TFG.core.entity.id.SesionId;
import com.tfg.API_TFG.core.repository.*;
import com.tfg.API_TFG.core.service.interfaces.SesionService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class SesionServiceImpl implements SesionService {
    private final SesionRepository sesionRepository;
    private final PeliculaRepository peliculaRepository;
    private final SalaRepository salaRepository;

    @Autowired
    public SesionServiceImpl(SesionRepository sesionRepository, PeliculaRepository peliculaRepository, SalaRepository salaRepository) {
        this.sesionRepository = sesionRepository;
        this.peliculaRepository = peliculaRepository;
        this.salaRepository = salaRepository;
    }

    @Override
    public List<SesionCompletaDTO> getAll() {
        return sesionRepository.findAll().stream()
                .map(SesionAdapter::toCompletoDTO)
                .toList();
    }

    @Override
    public List<SesionCompletaDTO> getAllBetweenHorarios(LocalDateTime horarioInicio, LocalDateTime horarioFin) {
        return sesionRepository.findByIdHorarioSesionBetween(horarioInicio, horarioFin).stream()
                .map(SesionAdapter::toCompletoDTO)
                .toList();
    }

    @Override
    public SesionCompletaDTO getSesion(Integer numSala, UUID peliculaId, LocalDateTime horario) {
        SesionCrudDTO sesionCrudDTO = new SesionCrudDTO(numSala, peliculaId, horario);
        SesionId id = toSesionId(sesionCrudDTO);
        Sesion sesion = sesionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("No existe la sesión para la sala %d con la película %s y para el horario %s.",
                                id.getNumSala(), id.getPeliculaId(), id.getHorarioSesion())
                ));
        return SesionAdapter.toCompletoDTO(sesion);
    }

    @Override
    @Transactional
    public SesionCompletaDTO createSesion(SesionCrudDTO sesionCrudDTO) {
        List<Sesion> sesiones = sesionRepository.findByIdHorarioSesionAndSalaNumSala(sesionCrudDTO.horario(), sesionCrudDTO.numSala());
        if(!sesiones.isEmpty()) throw new EntityExistsException(
                String.format("Ya existe una sesión para la sala %d con horario %s", sesionCrudDTO.numSala(), sesionCrudDTO.horario())
        );
        Pelicula pelicula = peliculaRepository.findById(sesionCrudDTO.peliculaId())
                .orElseThrow(() -> new EntityNotFoundException("No existe la película con ID " + sesionCrudDTO.peliculaId()));
        Sala sala = salaRepository.findById(sesionCrudDTO.numSala())
                .orElseThrow(() -> new EntityNotFoundException("No existe la sala " + sesionCrudDTO.numSala()));
        SesionId id = toSesionId(sesionCrudDTO);
        if (sesionRepository.existsById(id)) {
            throw new EntityExistsException(String.format(
                    "Ya existe una sesión en la sala %d para la película %s con horario %s",
                    sesionCrudDTO.numSala(),
                    pelicula.getNombre(),
                    sesionCrudDTO.horario()
            ));
        }
        Sesion sesion = new Sesion();
        sesion.setId(id);
        sesion.setSala(sala);
        sesion.setPelicula(pelicula);
        sesion.setHorario(sesionCrudDTO.horario());
        sesion = sesionRepository.save(sesion);
        return SesionAdapter.toCompletoDTO(sesion);
    }

    @Override
    @Transactional
    public SesionCompletaDTO deleteSesion(SesionCrudDTO sesionCrudDTO) {
        SesionId id = toSesionId(sesionCrudDTO);
        Sesion sesion = sesionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("No existe la sesión en la sala %d con horario %s",
                                sesionCrudDTO.numSala(),
                                sesionCrudDTO.horario())
                ));
        sesionRepository.delete(sesion);
        return SesionAdapter.toCompletoDTO(sesion);
    }

    private SesionId toSesionId(SesionCrudDTO sesionCrudDTO) {
        return new SesionId(
                sesionCrudDTO.numSala(),
                sesionCrudDTO.peliculaId(),
                sesionCrudDTO.horario()
        );
    }
}
