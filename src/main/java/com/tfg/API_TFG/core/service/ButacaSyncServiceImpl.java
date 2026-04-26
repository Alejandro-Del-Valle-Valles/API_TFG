package com.tfg.API_TFG.core.service;

import com.tfg.API_TFG.core.dto.ButacaDTO;
import com.tfg.API_TFG.core.dto.ButacasStatusResponse;
import com.tfg.API_TFG.core.dto.HoldButacaRequest;
import com.tfg.API_TFG.core.dto.HoldTokenResponse;
import com.tfg.API_TFG.core.dto.ReleaseButacaRequest;
import com.tfg.API_TFG.core.entity.BloqueoButaca;
import com.tfg.API_TFG.core.entity.Entrada;
import com.tfg.API_TFG.core.entity.Sesion;
import com.tfg.API_TFG.core.entity.id.EntradaId;
import com.tfg.API_TFG.core.entity.id.SesionId;
import com.tfg.API_TFG.core.repository.BloqueoButacaRepository;
import com.tfg.API_TFG.core.repository.EntradaRepository;
import com.tfg.API_TFG.core.repository.SesionRepository;
import com.tfg.API_TFG.core.service.interfaces.ButacaSyncService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ButacaSyncServiceImpl implements ButacaSyncService {

    private static final int BUTACAS_POR_FILA = 10;
    private static final int HOLD_MINUTOS = 10;

    private final SesionRepository sesionRepository;
    private final EntradaRepository entradaRepository;
    private final BloqueoButacaRepository bloqueoButacaRepository;

    public ButacaSyncServiceImpl(SesionRepository sesionRepository,
                                 EntradaRepository entradaRepository,
                                 BloqueoButacaRepository bloqueoButacaRepository) {
        this.sesionRepository = sesionRepository;
        this.entradaRepository = entradaRepository;
        this.bloqueoButacaRepository = bloqueoButacaRepository;
    }

    @Override
    public HoldTokenResponse createHoldToken(Integer numSala, UUID peliculaId, LocalDateTime horario) {
        SesionId sesionId = new SesionId(numSala, peliculaId, horario);
        if (!sesionRepository.existsById(sesionId)) {
            throw new EntityNotFoundException("La sesion indicada no existe.");
        }

        LocalDateTime expira = LocalDateTime.now().plusMinutes(HOLD_MINUTOS);
        return new HoldTokenResponse(UUID.randomUUID().toString(), expira);
    }

    @Override
    @Transactional
    public void releaseHoldToken(Integer numSala, UUID peliculaId, LocalDateTime horario, String token) {
        SesionId sesionId = new SesionId(numSala, peliculaId, horario);
        if (!sesionRepository.existsById(sesionId)) {
            throw new EntityNotFoundException("La sesion indicada no existe.");
        }

        bloqueoButacaRepository.deleteBySesion_IdAndToken(sesionId, token);
    }

    @Override
    @Transactional
    public void holdSeat(Integer numSala, UUID peliculaId, LocalDateTime horario, HoldButacaRequest req) {
        LocalDateTime now = LocalDateTime.now();
        cleanupExpired(now);

        SesionId sesionId = new SesionId(numSala, peliculaId, horario);
        Sesion sesion = sesionRepository.findById(sesionId)
                .orElseThrow(() -> new EntityNotFoundException("La sesion indicada no existe."));

        validarRangoButaca(sesion, req.fila(), req.butaca());

        EntradaId entradaId = new EntradaId(sesionId, req.fila(), req.butaca());
        if (entradaRepository.existsById(entradaId)) {
            throw new EntityExistsException("Butaca ya comprada");
        }

        boolean bloqueadaPorOtro = bloqueoButacaRepository
                .existsBySesion_IdAndFilaAndButacaAndExpiraGreaterThanEqualAndTokenNot(
                        sesionId,
                        req.fila(),
                        req.butaca(),
                        now,
                        req.token()
                );
        if (bloqueadaPorOtro) {
            throw new EntityExistsException("Butaca bloqueada por otro usuario");
        }

        BloqueoButaca bloqueo = bloqueoButacaRepository
                .findBySesion_IdAndFilaAndButacaAndTokenAndExpiraGreaterThanEqual(
                        sesionId,
                        req.fila(),
                        req.butaca(),
                        req.token(),
                        now
                )
                .orElseGet(BloqueoButaca::new);

        bloqueo.setSesion(sesion);
        bloqueo.setFila(req.fila());
        bloqueo.setButaca(req.butaca());
        bloqueo.setToken(req.token());
        bloqueo.setExpira(now.plusMinutes(HOLD_MINUTOS));
        bloqueoButacaRepository.save(bloqueo);
    }

    @Override
    @Transactional
    public void releaseSeat(Integer numSala, UUID peliculaId, LocalDateTime horario, ReleaseButacaRequest req) {
        SesionId sesionId = new SesionId(numSala, peliculaId, horario);
        bloqueoButacaRepository.deleteBySesion_IdAndFilaAndButacaAndToken(
                sesionId,
                req.fila(),
                req.butaca(),
                req.token()
        );
    }

    @Override
    @Transactional
    public ButacasStatusResponse getStatus(Integer numSala, UUID peliculaId, LocalDateTime horario) {
        LocalDateTime now = LocalDateTime.now();
        cleanupExpired(now);

        SesionId sesionId = new SesionId(numSala, peliculaId, horario);
        if (!sesionRepository.existsById(sesionId)) {
            throw new EntityNotFoundException("La sesion indicada no existe.");
        }

        List<ButacaDTO> ocupadas = entradaRepository.findByIdSesionId(sesionId).stream()
                .map(entrada -> new ButacaDTO(entrada.getId().getFila(), entrada.getId().getButaca()))
                .toList();

        List<ButacaDTO> bloqueadas = bloqueoButacaRepository.findBySesion_IdAndExpiraGreaterThanEqual(sesionId, now).stream()
                .map(bloqueo -> new ButacaDTO(bloqueo.getFila(), bloqueo.getButaca()))
                .toList();

        return new ButacasStatusResponse(ocupadas, bloqueadas);
    }

    private void validarRangoButaca(Sesion sesion, int fila, int butaca) {
        int aforo = sesion.getSala().getAforo();
        int totalFilas = (int) Math.ceil((double) aforo / BUTACAS_POR_FILA);

        if (fila < 1 || fila > totalFilas) {
            throw new IllegalArgumentException("Fila fuera de rango para el aforo de la sala.");
        }

        int butacasUltimaFila = aforo % BUTACAS_POR_FILA == 0 ? BUTACAS_POR_FILA : aforo % BUTACAS_POR_FILA;
        int maxButacaFila = fila == totalFilas ? butacasUltimaFila : BUTACAS_POR_FILA;
        if (butaca < 1 || butaca > maxButacaFila) {
            throw new IllegalArgumentException("Butaca fuera de rango para el aforo de la sala.");
        }
    }

    private void cleanupExpired(LocalDateTime now) {
        bloqueoButacaRepository.deleteExpired(now);
    }
}

