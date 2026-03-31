package com.tfg.API_TFG.core.service;

import com.tfg.API_TFG.adapter.PeliculaAdapter;
import com.tfg.API_TFG.core.dto.*;
import com.tfg.API_TFG.core.entity.Credito;
import com.tfg.API_TFG.core.entity.Participante;
import com.tfg.API_TFG.core.entity.Pelicula;
import com.tfg.API_TFG.core.entity.id.CreditoId;
import com.tfg.API_TFG.core.enums.RolParticipante;
import com.tfg.API_TFG.core.repository.CreditoRepository;
import com.tfg.API_TFG.core.repository.ParticipanteRepository;
import com.tfg.API_TFG.core.service.interfaces.PeliculaService;
import com.tfg.API_TFG.core.repository.PeliculaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PeliculaServiceImpl implements PeliculaService {
    private final PeliculaRepository peliculaRepository;
    private final ParticipanteRepository participanteRepository;

    @Autowired
    public PeliculaServiceImpl(PeliculaRepository peliculaRepository, CreditoRepository creditoRepository,
                               ParticipanteRepository participanteRepository) {
        this.peliculaRepository = peliculaRepository;
        this.participanteRepository = participanteRepository;
    }

    @Override
    public List<PeliculaDTO> getAll() {
        return peliculaRepository.findAll().stream()
                .map(PeliculaAdapter::toDTO)
                .toList();
    }

    @Override
    public List<PeliculaCompletoDTO> getAllCompleto() {
        return peliculaRepository.findAll().stream()
                .map(PeliculaAdapter::toCompletoDTO)
                .toList();
    }

    @Override
    public List<PeliculaDTO> getByNombreContainingIgonreCase(String nombre) {
        return peliculaRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(PeliculaAdapter::toDTO)
                .toList();
    }

    @Override
    public List<PeliculaCompletoDTO> getCompletoByNombreContainingIgnoreCase(String nombre) {
        return peliculaRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(PeliculaAdapter::toCompletoDTO)
                .toList();
    }

    @Override
    public PeliculaCompletoDTO getCompletoById(UUID id) {
        Pelicula pelicula = peliculaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No existe la película con ID: " + id));
        return PeliculaAdapter.toCompletoDTO(pelicula);
    }

    @Override
    @Transactional
    public PeliculaCompletoDTO createPelicula(PeliculaCreateDTO peliculaCreateDTO) {
        Pelicula pelicula = new Pelicula();
        pelicula.setNombre(peliculaCreateDTO.nombre());
        pelicula.setDescripcion(peliculaCreateDTO.descripcion());
        pelicula.setDuracion(peliculaCreateDTO.duracion());
        pelicula.setPortada(peliculaCreateDTO.url());
        pelicula.setCalificacionEdad(peliculaCreateDTO.edad());
        pelicula = peliculaRepository.save(pelicula);

        List<Integer> participanteIds = peliculaCreateDTO.participantes().stream()
                .map(ParticipanteCreateDTO::id)
                .distinct()
                .toList();

        List<Participante> participantes = participanteRepository.findAllById(participanteIds);
        if (participantes.size() != participanteIds.size()) {
            List<Integer> encontrados = participantes.stream().map(Participante::getId).toList();
            List<Integer> noEncontrados = participanteIds.stream().filter(id -> !encontrados.contains(id)).toList();
            throw new EntityNotFoundException("No se encontraron los siguientes participantes: " + noEncontrados);
        }

        Map<Integer, Participante> participantesMap = participantes.stream()
                .collect(Collectors.toMap(Participante::getId, p -> p));
        Set<String> clavesUsadas = new HashSet<>();

        for (ParticipanteCreateDTO participanteDTO : peliculaCreateDTO.participantes()) {
            Participante participante = participantesMap.get(participanteDTO.id());

            if (participanteDTO.roles() == null || participanteDTO.roles().isEmpty())
                throw new IllegalArgumentException(
                        "El participante " + participante.getNombre() + " debe tener al menos un rol"
                );

            for (RolParticipante rol : participanteDTO.roles()) {
                String key = participante.getId() + "|" + rol.name();
                if (!clavesUsadas.add(key)) continue;

                CreditoId creditoId = new CreditoId(pelicula.getId(), participante.getId(), rol);
                Credito credito = new Credito();
                credito.setId(creditoId);
                credito.setPelicula(pelicula);
                credito.setParticipante(participante);
                credito.setRol(rol);
                pelicula.getCreditos().add(credito);
            }
        }

        pelicula = peliculaRepository.save(pelicula);
        return PeliculaAdapter.toCompletoDTO(pelicula);
    }

    @Override
    @Transactional
    public PeliculaCompletoDTO updatePeliculaCompleto(PeliculaCompletoDTO peliculaCompletoDTO) {
        Pelicula pelicula = peliculaRepository.findById(peliculaCompletoDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "No existe la película con ID " + peliculaCompletoDTO.getId()
                ));
        actualizarCamposBasicos(pelicula, peliculaCompletoDTO);
        actualizarParticipantes(pelicula, peliculaCompletoDTO.getParticipantes());
        pelicula = peliculaRepository.save(pelicula);
        return PeliculaAdapter.toCompletoDTO(pelicula);
    }

    @Override
    @Transactional
    public PeliculaDTO deletePelicula(UUID id) {
        Pelicula pelicula = peliculaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No existe la película con ID " + id));
        peliculaRepository.delete(pelicula);
        return PeliculaAdapter.toDTO(pelicula);
    }

    /**
     * Actualiza los campos básicos de una película en base a los datos del DTO pasado.
     * @param pelicula Pelicula a actualizar
     * @param dto PelículaCompletaDTO a actualizar.
     */
    private void actualizarCamposBasicos(Pelicula pelicula, PeliculaCompletoDTO dto) {
        pelicula.setNombre(dto.getNombre());
        pelicula.setDescripcion(dto.getDescripcion());
        pelicula.setDuracion(dto.getDuracion());
        pelicula.setPortada(dto.getUrl());
        pelicula.setCalificacionEdad(dto.getEdad());
    }

    /**
     * Actualiza la relación de una película con los cŕedito y participantes para eliminar los que ya no aparezcan, modificar
     * los que han cambiado, y añadir los nuevos.
     * @param pelicula Película sobre la que actuar.
     * @param participantesDTO List de participantes de la película.
     */
    private void actualizarParticipantes(Pelicula pelicula, List<ParticipanteCompletoDTO> participantesDTO) {
        Map<Integer, Set<RolParticipante>> nuevosParticipantesRoles = construirMapaParticipantesRoles(participantesDTO);
        eliminarCreditosObsoletos(pelicula, nuevosParticipantesRoles);
        agregarNuevosCreditos(pelicula, participantesDTO, nuevosParticipantesRoles);
    }

    /**
     * Genera un Map con los roles de cada participante en base a la lista de DTOs
     * @param participantesDTO List con ParticipanteCompletoDTO para generar el mapa de Participantes-Roles
     * @return Map<Integer, Set<RolParticipante>>
     */
    private Map<Integer, Set<RolParticipante>> construirMapaParticipantesRoles(
            List<ParticipanteCompletoDTO> participantesDTO) {
        Map<Integer, Set<RolParticipante>> mapa = new HashMap<>();
        for (ParticipanteCompletoDTO dto : participantesDTO) {
            mapa.put(dto.getId(), new HashSet<>(dto.getRoles()));
        }
        return mapa;
    }

    /**
     * Elimina la relación entre Película-Credito-Participante de aquellos participantes que ya no participan en una pleícula.
     * @param pelicula Pelicula sobre la que actuar.
     * @param nuevosParticipantesRoles Map<Integer, Set<RolParticipante>> con los participantes nuevos de la película.
     */
    private void eliminarCreditosObsoletos(
            Pelicula pelicula,
            Map<Integer, Set<RolParticipante>> nuevosParticipantesRoles) {

        List<Credito> creditosAEliminar = pelicula.getCreditos().stream()
                .filter(credito -> {
                    Integer participanteId = credito.getParticipante().getId();
                    RolParticipante rol = credito.getRol();
                    return !nuevosParticipantesRoles.containsKey(participanteId) ||
                            !nuevosParticipantesRoles.get(participanteId).contains(rol);
                })
                .toList();

        pelicula.getCreditos().removeAll(creditosAEliminar);
    }

    /**
     * Crea las relaciones entre Pelicula-Credito-Participante nuevas.
     * @param pelicula Pelicula sobre la que actuar.
     * @param participantesDTO List de ParticipanteCompletoDTO con los nuevos participantes.
     * @param nuevosParticipantesRoles Map con el ID y créditos de los nuevos participantes para crear la relación.
     */
    private void agregarNuevosCreditos(Pelicula pelicula, List<ParticipanteCompletoDTO> participantesDTO,
                                       Map<Integer, Set<RolParticipante>> nuevosParticipantesRoles) {
        Map<Integer, Set<RolParticipante>> creditosActuales = new HashMap<>();
        for (Credito credito : pelicula.getCreditos()) {
            Integer participanteId = credito.getParticipante().getId();
            creditosActuales
                    .computeIfAbsent(participanteId, k -> new HashSet<>())
                    .add(credito.getRol());
        }
        for (ParticipanteCompletoDTO participanteDTO : participantesDTO) {
            Participante participante = participanteRepository.findById(participanteDTO.getId())
                    .orElseThrow(() -> new EntityNotFoundException("No existe el participante con ID " + participanteDTO.getId()));
            Set<RolParticipante> rolesActuales = creditosActuales.getOrDefault(participanteDTO.getId(),
                    Collections.emptySet());
            for (RolParticipante rol : participanteDTO.getRoles()) {
                if (!rolesActuales.contains(rol)) {
                    Credito nuevoCredito = new Credito();
                    nuevoCredito.setRol(rol);
                    nuevoCredito.setParticipante(participante);
                    pelicula.addCredito(nuevoCredito);
                }
            }
        }
    }
}
