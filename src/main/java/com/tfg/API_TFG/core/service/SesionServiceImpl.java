package com.tfg.API_TFG.core.service;

import com.tfg.API_TFG.adapter.SesionAdapter;
import com.tfg.API_TFG.core.dto.EntradaDTO;
import com.tfg.API_TFG.core.dto.SesionCrudDTO;
import com.tfg.API_TFG.core.dto.SesionCompletaDTO;
import com.tfg.API_TFG.core.entity.*;
import com.tfg.API_TFG.core.entity.id.EntradaId;
import com.tfg.API_TFG.core.entity.id.SesionId;
import com.tfg.API_TFG.core.repository.EntradaRepository;
import com.tfg.API_TFG.core.repository.PeliculaRepository;
import com.tfg.API_TFG.core.repository.SalaRepository;
import com.tfg.API_TFG.core.service.interfaces.SesionService;
import com.tfg.API_TFG.core.repository.SesionRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SesionServiceImpl implements SesionService {
    private final SesionRepository sesionRepository;
    private final PeliculaRepository peliculaRepository;
    private final SalaRepository salaRepository;
    private final EntradaRepository entradaRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public SesionServiceImpl(SesionRepository sesionRepository, PeliculaRepository peliculaRepository,
                             SalaRepository salaRepository, EntradaRepository entradaRepository) {
        this.sesionRepository = sesionRepository;
        this.peliculaRepository = peliculaRepository;
        this.salaRepository = salaRepository;
        this.entradaRepository = entradaRepository;
    }

    @Override
    public List<SesionCompletaDTO> getAll() {
        return sesionRepository.findAll().stream()
                .map(SesionAdapter::toCompletoDTO)
                .toList();
    }

    @Override
    public List<SesionCompletaDTO> getAllBetweenHorarios(LocalDateTime horarioInicio, LocalDateTime horarioFin) {
        return sesionRepository.findByHorarioBetween(horarioInicio, horarioFin).stream()
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
    public SesionCompletaDTO updateSesion(SesionCrudDTO sesionActual, Integer numSalaNueva, UUID peliculaIdNueva,
                                          LocalDateTime horarioNuevo) {

        if (numSalaNueva == null && peliculaIdNueva == null && horarioNuevo == null)
            throw new IllegalArgumentException("Debes introducir al menos un nuevo valor para actualizar una sesión.");
        SesionId idActual = new SesionId(
                sesionActual.numSala(),
                sesionActual.peliculaId(),
                sesionActual.horario()
        );

        Sesion sesionOld = sesionRepository.findById(idActual)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("No existe la sesión en la sala %d para la película %s con horario %s",
                                sesionActual.numSala(), sesionActual.peliculaId(), sesionActual.horario())
                ));
        Integer numSalaFinal = (numSalaNueva != null) ? numSalaNueva : sesionActual.numSala();
        UUID peliculaIdFinal = (peliculaIdNueva != null) ? peliculaIdNueva : sesionActual.peliculaId();
        LocalDateTime horarioFinal = (horarioNuevo != null) ? horarioNuevo : sesionActual.horario();

        SesionId idNuevo = new SesionId(numSalaFinal, peliculaIdFinal, horarioFinal);
        if (idNuevo.equals(idActual)) return SesionAdapter.toCompletoDTO(sesionOld);

        if (sesionRepository.existsById(idNuevo))
            throw new EntityExistsException(String.format(
                    "Ya existe una sesión en la sala %d para la película %s con horario %s",
                    numSalaFinal, peliculaIdFinal, horarioFinal));

        Sala salaNueva = salaRepository.findById(numSalaFinal)
                .orElseThrow(() -> new EntityNotFoundException("No existe la sala " + numSalaFinal));
        Pelicula peliculaNueva = peliculaRepository.findById(peliculaIdFinal)
                .orElseThrow(() -> new EntityNotFoundException("No existe la película con ID " + peliculaIdFinal));
        Sesion sesionNew = new Sesion();
        sesionNew.setId(idNuevo);
        sesionNew.setSala(salaNueva);
        sesionNew.setPelicula(peliculaNueva);
        sesionNew.setHorario(horarioFinal);
        sesionNew = sesionRepository.saveAndFlush(sesionNew);
        List<Entrada> entradasOld = new ArrayList<>(sesionOld.getEntradas());

        for (Entrada entradaOld : entradasOld) {
            LineaCompra linea = entradaOld.getLineaCompra();

            EntradaId entradaIdNew = new EntradaId(
                    idNuevo.getNumSala(),
                    idNuevo.getPeliculaId(),
                    idNuevo.getHorarioSesion(),
                    entradaOld.getId().getFila(),
                    entradaOld.getId().getButaca()
            );

            if (entradaRepository.existsById(entradaIdNew)) {
                throw new EntityExistsException("Ya existe la entrada " + entradaIdNew);
            }

            Entrada entradaNew = new Entrada();
            entradaNew.setId(entradaIdNew);
            entradaNew.setPrecio(entradaOld.getPrecio());
            entradaNew.setSesion(sesionNew);
            entradaNew.setLineaCompra(linea);
            if (linea != null)
                linea.setEntrada(entradaNew);
            sesionNew.addEntrada(entradaNew);
        }
        sesionRepository.saveAndFlush(sesionNew);
        for (Entrada entradaOld : entradasOld) {
            entradaOld.setLineaCompra(null);
            entradaOld.setSesion(null);
        }
        sesionOld.getEntradas().clear();

        sesionRepository.delete(sesionOld);
        sesionRepository.flush();

        return SesionAdapter.toCompletoDTO(sesionNew);
    }

    @Override
    @Transactional
    public SesionCompletaDTO deleteSesion(SesionCrudDTO sesionCrudDTO) {
        return SesionAdapter.toCompletoDTO(eliminarSesion(sesionCrudDTO));
    }

    /**
     * Elimina la sesión de la BBDD.
     * @param sesionCrudDTO SesionCrudDTO Información de la sesión a eliminar
     * @return Sesion de la sesión eliminada.
     */
    private Sesion eliminarSesion(SesionCrudDTO sesionCrudDTO) {
        SesionId id = toSesionId(sesionCrudDTO);
        Sesion sesion = sesionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("No existe la sesión en la sala %d con horario %s",
                                sesionCrudDTO.numSala(),
                                sesionCrudDTO.horario())
                ));
        sesionRepository.delete(sesion);
        return sesion;
    }

    private SesionId toSesionId(SesionCrudDTO sesionCrudDTO) {
        return new SesionId(
                sesionCrudDTO.numSala(),
                sesionCrudDTO.peliculaId(),
                sesionCrudDTO.horario()
        );
    }
}
