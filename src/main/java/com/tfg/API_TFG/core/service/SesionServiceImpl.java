package com.tfg.API_TFG.core.service;

import com.tfg.API_TFG.adapter.SesionAdapter;
import com.tfg.API_TFG.core.dto.EntradaDTO;
import com.tfg.API_TFG.core.dto.SesionCrudDTO;
import com.tfg.API_TFG.core.dto.SesionCompletaDTO;
import com.tfg.API_TFG.core.entity.*;
import com.tfg.API_TFG.core.entity.id.EntradaId;
import com.tfg.API_TFG.core.entity.id.LineaId;
import com.tfg.API_TFG.core.entity.id.SesionId;
import com.tfg.API_TFG.core.repository.*;
import com.tfg.API_TFG.core.service.interfaces.SesionService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class SesionServiceImpl implements SesionService {
    private final SesionRepository sesionRepository;
    private final PeliculaRepository peliculaRepository;
    private final SalaRepository salaRepository;
    private final EntradaRepository entradaRepository;
    private final LineaCompraRepository lineaCompraRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public SesionServiceImpl(SesionRepository sesionRepository, PeliculaRepository peliculaRepository,
                             SalaRepository salaRepository, EntradaRepository entradaRepository, LineaCompraRepository lineaCompraRepository) {
        this.sesionRepository = sesionRepository;
        this.peliculaRepository = peliculaRepository;
        this.salaRepository = salaRepository;
        this.entradaRepository = entradaRepository;
        this.lineaCompraRepository = lineaCompraRepository;
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
    public SesionCompletaDTO updateSesion(SesionCrudDTO sesionActual, Integer numSalaNueva, UUID peliculaIdNueva,
                                          LocalDateTime horarioNuevo) {
        if (numSalaNueva == null && peliculaIdNueva == null && horarioNuevo == null)
            throw new IllegalArgumentException("Debes introducir al menos un nuevo valor para actualizar una sesión.");

        SesionId idActual = new SesionId(sesionActual.numSala(), sesionActual.peliculaId(), sesionActual.horario());

        Sesion sesionOld = sesionRepository.findById(idActual)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("No existe la sesión en la sala %d para la película %s con horario %s",
                                sesionActual.numSala(), sesionActual.peliculaId(), sesionActual.horario())
                ));

        Integer numSalaFinal = (numSalaNueva != null) ? numSalaNueva : sesionActual.numSala();
        UUID peliculaIdFinal = (peliculaIdNueva != null) ? peliculaIdNueva : sesionActual.peliculaId();
        LocalDateTime horarioFinal = (horarioNuevo != null) ? horarioNuevo : sesionActual.horario();

        if (!Objects.equals(numSalaFinal, sesionOld.getId().getNumSala())
                || !Objects.equals(horarioFinal, sesionOld.getHorario())) {
            List<Sesion> sesionesOcupadas = sesionRepository.findByIdHorarioSesionAndSalaNumSala(horarioFinal, numSalaFinal);
            if (!sesionesOcupadas.isEmpty()) throw new EntityExistsException(
                    String.format("Ya existe una sesión para la sala %d con horario %s", numSalaFinal, horarioFinal)
            );
        }

        SesionId idNuevo = new SesionId(numSalaFinal, peliculaIdFinal, horarioFinal);

        if (idNuevo.equals(idActual)) return SesionAdapter.toCompletoDTO(sesionOld);

        if (sesionRepository.existsById(idNuevo))
            throw new EntityExistsException(String.format(
                    "Ya existe una sesión en la sala %d para la película %s con horario %s",
                    numSalaFinal, peliculaIdFinal, horarioFinal
            ));

        record EntradaSnapshot(Integer fila, Integer butaca, BigDecimal precio, LineaId lineaId) {}
        List<EntradaSnapshot> snapshot = sesionOld.getEntradas().stream()
                .map(e -> new EntradaSnapshot(e.getId().getFila(), e.getId().getButaca(), e.getPrecio(),
                        e.getLineaCompra() != null ? e.getLineaCompra().getId() : null))
                .toList();

        sesionRepository.delete(sesionOld);
        sesionRepository.flush();
        entityManager.clear();

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

        List<Map.Entry<EntradaId, LineaId>> pendientesLinea = new ArrayList<>();

        for (EntradaSnapshot s : snapshot) {
            Entrada entradaNew = new Entrada();

            EntradaId entradaIdNew = new EntradaId();
            entradaIdNew.setSesionId(null);
            entradaIdNew.setFila(s.fila());
            entradaIdNew.setButaca(s.butaca());

            entradaNew.setId(entradaIdNew);
            entradaNew.setPrecio(s.precio());

            entradaNew.setSesion(sesionNew);
            sesionNew.getEntradas().add(entradaNew);

            EntradaId finalId = new EntradaId(idNuevo, s.fila(), s.butaca());
            pendientesLinea.add(new AbstractMap.SimpleEntry<>(finalId, s.lineaId()));
        }

        sesionNew = sesionRepository.saveAndFlush(sesionNew);

        for (Map.Entry<EntradaId, LineaId> p : pendientesLinea) {
            LineaId lineaId = p.getValue();
            if (lineaId == null) continue;

            LineaCompra linea = lineaCompraRepository.findById(lineaId)
                    .orElseThrow(() -> new EntityNotFoundException("No existe la línea de compra " + lineaId));

            Entrada entradaManaged = entradaRepository.findById(p.getKey())
                    .orElseThrow(() -> new EntityNotFoundException("No existe la entrada " + p.getKey()));

            linea.setEntrada(entradaManaged);
            entradaManaged.setLineaCompra(linea);
        }

        lineaCompraRepository.flush();
        return SesionAdapter.toCompletoDTO(sesionNew);
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
