package com.tfg.API_TFG.service;

import com.tfg.API_TFG.core.dto.*;
import com.tfg.API_TFG.core.entity.BloqueoButaca;
import com.tfg.API_TFG.core.entity.Pelicula;
import com.tfg.API_TFG.core.entity.Sala;
import com.tfg.API_TFG.core.entity.Sesion;
import com.tfg.API_TFG.core.entity.Usuario;
import com.tfg.API_TFG.core.entity.id.EntradaId;
import com.tfg.API_TFG.core.entity.id.SesionId;
import com.tfg.API_TFG.core.repository.*;
import com.tfg.API_TFG.core.service.CompraServiceImpl;
import com.tfg.API_TFG.core.service.EmailService;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompraServiceImplTest {

    @Mock CompraRepository compraRepository;
    @Mock UsuarioRepository usuarioRepository;
    @Mock EntradaRepository entradaRepository;
    @Mock BloqueoButacaRepository bloqueoButacaRepository;
    @Mock SesionRepository sesionRepository;
    @Mock ProductoRepository productoRepository;
    @Mock EmailService emailService;

    @InjectMocks CompraServiceImpl compraService;

    @Test
    void createCompra_conButacaBloqueadaYTokenValido_consumebloqueoYAjustaEntradaIdCompuesta() {
        UUID peliculaId = UUID.randomUUID();
        LocalDateTime horario = LocalDateTime.now().plusDays(1).withSecond(0).withNano(0);
        SesionId sesionId = new SesionId(1, peliculaId, horario);

        Sesion sesion = new Sesion();
        sesion.setId(sesionId);
        Sala sala = new Sala();
        sala.setNumSala(1);
        sala.setAforo(100);
        sesion.setSala(sala);
        Pelicula pelicula = new Pelicula();
        pelicula.setNombre("Interstellar");
        sesion.setPelicula(pelicula);

        Usuario usuario = new Usuario();
        usuario.setCorreo("anonimo@correo.com");

        EntradaDTO entradaDTO = new EntradaDTO(new SesionDTO(1, horario, peliculaId), 2, 4, new BigDecimal("7.50"));
        LineaCompraEntradaDTO linea = new LineaCompraEntradaDTO(1, entradaDTO);
        CompraDTO compraDTO = new CompraDTO(usuario.getCorreo(), "token-123", List.of(linea));

        when(usuarioRepository.findByCorreo(usuario.getCorreo())).thenReturn(Optional.of(usuario));
        when(sesionRepository.findById(sesionId)).thenReturn(Optional.of(sesion));
        when(entradaRepository.existsById(new EntradaId(sesionId, 2, 4))).thenReturn(false);
        when(bloqueoButacaRepository.findBySesion_IdAndFilaAndButacaAndTokenAndExpiraGreaterThanEqual(
                eq(sesionId), eq(2), eq(4), eq("token-123"), any(LocalDateTime.class)
        )).thenReturn(Optional.of(new BloqueoButaca()));
        when(compraRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        CompraDTO result = compraService.createCompra(compraDTO);

        ArgumentCaptor<com.tfg.API_TFG.core.entity.Compra> compraCaptor = ArgumentCaptor.forClass(com.tfg.API_TFG.core.entity.Compra.class);
        verify(compraRepository).save(compraCaptor.capture());
        EntradaId entradaIdGuardada = compraCaptor.getValue().getLineaCompras().get(0).getEntrada().getId();

        assertThat(entradaIdGuardada.getSesionId()).isEqualTo(sesionId);
        assertThat(result.holdToken()).isEqualTo("token-123");
        verify(bloqueoButacaRepository).deleteByToken("token-123");
        verify(emailService).enviarCompraHtml(eq(usuario.getCorreo()), anyString(), anyString());
    }

    @Test
    void createCompra_sinBloqueoValido_lanzaConflicto409() {
        UUID peliculaId = UUID.randomUUID();
        LocalDateTime horario = LocalDateTime.now().plusDays(1).withSecond(0).withNano(0);
        SesionId sesionId = new SesionId(1, peliculaId, horario);

        Sesion sesion = new Sesion();
        sesion.setId(sesionId);

        Usuario usuario = new Usuario();
        usuario.setCorreo("anonimo@correo.com");

        EntradaDTO entradaDTO = new EntradaDTO(new SesionDTO(1, horario, peliculaId), 2, 4, new BigDecimal("7.50"));
        LineaCompraEntradaDTO linea = new LineaCompraEntradaDTO(1, entradaDTO);
        CompraDTO compraDTO = new CompraDTO(usuario.getCorreo(), "token-123", List.of(linea));

        when(usuarioRepository.findByCorreo(usuario.getCorreo())).thenReturn(Optional.of(usuario));
        when(sesionRepository.findById(sesionId)).thenReturn(Optional.of(sesion));
        when(entradaRepository.existsById(new EntradaId(sesionId, 2, 4))).thenReturn(false);
        when(bloqueoButacaRepository.findBySesion_IdAndFilaAndButacaAndTokenAndExpiraGreaterThanEqual(
                eq(sesionId), eq(2), eq(4), eq("token-123"), any(LocalDateTime.class)
        )).thenReturn(Optional.empty());

        assertThatThrownBy(() -> compraService.createCompra(compraDTO))
                .isInstanceOf(EntityExistsException.class)
                .hasMessage("Bloqueo expirado o no pertenece al token");

        verify(compraRepository, never()).save(any());
        verify(bloqueoButacaRepository, never()).deleteByToken(anyString());
    }
}

