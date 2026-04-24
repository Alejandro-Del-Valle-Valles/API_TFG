package com.tfg.API_TFG.core.service.interfaces;

import com.tfg.API_TFG.core.dto.ButacasStatusResponse;
import com.tfg.API_TFG.core.dto.HoldButacaRequest;
import com.tfg.API_TFG.core.dto.HoldTokenResponse;
import com.tfg.API_TFG.core.dto.ReleaseButacaRequest;

import java.time.LocalDateTime;
import java.util.UUID;

public interface ButacaSyncService {
    HoldTokenResponse createHoldToken(Integer numSala, UUID peliculaId, LocalDateTime horario);

    void holdSeat(Integer numSala, UUID peliculaId, LocalDateTime horario, HoldButacaRequest req);

    void releaseSeat(Integer numSala, UUID peliculaId, LocalDateTime horario, ReleaseButacaRequest req);

    ButacasStatusResponse getStatus(Integer numSala, UUID peliculaId, LocalDateTime horario);
}

