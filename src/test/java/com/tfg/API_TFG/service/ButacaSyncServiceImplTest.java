package com.tfg.API_TFG.service;

import com.tfg.API_TFG.core.dto.ButacaDTO;
import com.tfg.API_TFG.core.dto.ButacasStatusResponse;
import com.tfg.API_TFG.core.dto.HoldButacaRequest;
import com.tfg.API_TFG.core.entity.BloqueoButaca;
import com.tfg.API_TFG.core.entity.Entrada;
import com.tfg.API_TFG.core.entity.Sala;
import com.tfg.API_TFG.core.entity.Sesion;
import com.tfg.API_TFG.core.entity.id.EntradaId;
import com.tfg.API_TFG.core.entity.id.SesionId;
import com.tfg.API_TFG.core.repository.BloqueoButacaRepository;
import com.tfg.API_TFG.core.repository.EntradaRepository;
import com.tfg.API_TFG.core.repository.SesionRepository;
import com.tfg.API_TFG.core.service.ButacaSyncServiceImpl;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ButacaSyncServiceImplTest {

    @Mock SesionRepository sesionRepository;
    @Mock EntradaRepository entradaRepository;
    @Mock BloqueoButacaRepository bloqueoButacaRepository;

    @InjectMocks ButacaSyncServiceImpl butacaSyncService;

    @Test
    void holdSeat_siYaExisteEntrada_lanzaConflictoButacaComprada() {
        SesionId sesionId = new SesionId(1, UUID.randomUUID(), LocalDateTime.now().plusDays(1).withSecond(0).withNano(0));
        Sesion sesion = sesionConAforo(100, sesionId);
        HoldButacaRequest request = new HoldButacaRequest("token-1", 3, 5);

        when(sesionRepository.findById(sesionId)).thenReturn(Optional.of(sesion));
        when(entradaRepository.existsById(new EntradaId(sesionId, 3, 5))).thenReturn(true);

        assertThatThrownBy(() -> butacaSyncService.holdSeat(
                sesionId.getNumSala(),
                sesionId.getPeliculaId(),
                sesionId.getHorarioSesion(),
                request
        )).isInstanceOf(EntityExistsException.class)
                .hasMessage("Butaca ya comprada");
    }

    @Test
    void getStatus_devuelveOcupadasYBloqueadas() {
        SesionId sesionId = new SesionId(1, UUID.randomUUID(), LocalDateTime.now().plusDays(1).withSecond(0).withNano(0));

        Entrada entrada = new Entrada();
        entrada.setId(new EntradaId(sesionId, 2, 3));

        BloqueoButaca bloqueo = new BloqueoButaca();
        bloqueo.setFila(4);
        bloqueo.setButaca(6);

        when(sesionRepository.existsById(sesionId)).thenReturn(true);
        when(entradaRepository.findByIdSesionId(sesionId)).thenReturn(List.of(entrada));
        when(bloqueoButacaRepository.findBySesion_IdAndExpiraGreaterThanEqual(eq(sesionId), any(LocalDateTime.class)))
                .thenReturn(List.of(bloqueo));

        ButacasStatusResponse status = butacaSyncService.getStatus(
                sesionId.getNumSala(),
                sesionId.getPeliculaId(),
                sesionId.getHorarioSesion()
        );

        assertThat(status.ocupadas()).containsExactly(new ButacaDTO(2, 3));
        assertThat(status.bloqueadas()).containsExactly(new ButacaDTO(4, 6));
    }

    @Test
    void releaseHoldToken_eliminaBloqueosDelTokenEnLaSesion() {
        SesionId sesionId = new SesionId(1, UUID.randomUUID(), LocalDateTime.now().plusDays(1).withSecond(0).withNano(0));
        String token = "token-1";

        when(sesionRepository.existsById(sesionId)).thenReturn(true);

        butacaSyncService.releaseHoldToken(
                sesionId.getNumSala(),
                sesionId.getPeliculaId(),
                sesionId.getHorarioSesion(),
                token
        );

        verify(bloqueoButacaRepository).deleteBySesion_IdAndToken(sesionId, token);
    }

    private Sesion sesionConAforo(int aforo, SesionId sesionId) {
        Sala sala = new Sala();
        sala.setAforo(aforo);

        Sesion sesion = new Sesion();
        sesion.setId(sesionId);
        sesion.setSala(sala);
        return sesion;
    }
}

