package com.tfg.API_TFG.service;

import com.tfg.API_TFG.core.dto.BanerDTO;
import com.tfg.API_TFG.core.entity.Baner;
import com.tfg.API_TFG.core.entity.Pelicula;
import com.tfg.API_TFG.core.repository.BanerRepository;
import com.tfg.API_TFG.core.repository.PeliculaRepository;
import com.tfg.API_TFG.core.service.BanerServiceImpl;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BanerServiceImplTest {

    @Mock
    BanerRepository banerRepository;

    @Mock
    PeliculaRepository peliculaRepository;

    @InjectMocks
    BanerServiceImpl banerService;

    @Test
    void updateBaner_actualizaDatosYConservaLaPeliculaAsociada() {
        UUID peliculaId = UUID.randomUUID();

        Pelicula pelicula = new Pelicula();
        pelicula.setId(peliculaId);

        Baner banerExistente = new Baner();
        banerExistente.setId(peliculaId);
        banerExistente.setPelicula(pelicula);
        banerExistente.setUrl("https://old/banner.jpg");
        banerExistente.setEmpieza(LocalDate.of(2026, 4, 1));
        banerExistente.setTermina(LocalDate.of(2026, 4, 10));

        BanerDTO cambios = new BanerDTO(
                UUID.randomUUID(),
                "https://new/banner.jpg",
                LocalDate.of(2026, 4, 5),
                LocalDate.of(2026, 4, 15)
        );

        when(banerRepository.findByUrl("https://old/banner.jpg")).thenReturn(Optional.of(banerExistente));
        when(banerRepository.existsByUrl("https://new/banner.jpg")).thenReturn(false);
        when(banerRepository.save(any(Baner.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BanerDTO result = banerService.updateBaner("https://old/banner.jpg", cambios);

        assertThat(result.url()).isEqualTo("https://new/banner.jpg");
        assertThat(result.empieza()).isEqualTo(LocalDate.of(2026, 4, 5));
        assertThat(result.termina()).isEqualTo(LocalDate.of(2026, 4, 15));
        assertThat(result.peliculaId()).isEqualTo(peliculaId);

        ArgumentCaptor<Baner> captor = ArgumentCaptor.forClass(Baner.class);
        verify(banerRepository).findByUrl("https://old/banner.jpg");
        verify(banerRepository).existsByUrl("https://new/banner.jpg");
        verify(banerRepository).save(captor.capture());
        verifyNoInteractions(peliculaRepository);

        Baner guardado = captor.getValue();
        assertThat(guardado.getPelicula()).isSameAs(pelicula);
        assertThat(guardado.getUrl()).isEqualTo("https://new/banner.jpg");
        assertThat(guardado.getEmpieza()).isEqualTo(LocalDate.of(2026, 4, 5));
        assertThat(guardado.getTermina()).isEqualTo(LocalDate.of(2026, 4, 15));
    }

    @Test
    void updateBaner_siLaUrlNoCambia_noComprobarConflictoDeUrlYPermitirUpdate() {
        UUID peliculaId = UUID.randomUUID();

        Pelicula pelicula = new Pelicula();
        pelicula.setId(peliculaId);

        Baner banerExistente = new Baner();
        banerExistente.setId(peliculaId);
        banerExistente.setPelicula(pelicula);
        banerExistente.setUrl("https://same/banner.jpg");
        banerExistente.setEmpieza(LocalDate.of(2026, 4, 1));
        banerExistente.setTermina(LocalDate.of(2026, 4, 10));

        BanerDTO cambios = new BanerDTO(
                UUID.randomUUID(),
                "https://same/banner.jpg",
                LocalDate.of(2026, 4, 2),
                LocalDate.of(2026, 4, 12)
        );

        when(banerRepository.findByUrl("https://same/banner.jpg")).thenReturn(Optional.of(banerExistente));
        when(banerRepository.save(any(Baner.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BanerDTO result = banerService.updateBaner("https://same/banner.jpg", cambios);

        assertThat(result.url()).isEqualTo("https://same/banner.jpg");
        assertThat(result.empieza()).isEqualTo(LocalDate.of(2026, 4, 2));
        assertThat(result.termina()).isEqualTo(LocalDate.of(2026, 4, 12));
        assertThat(result.peliculaId()).isEqualTo(peliculaId);

        verify(banerRepository).findByUrl("https://same/banner.jpg");
        verify(banerRepository, never()).existsByUrl(anyString());
        verify(banerRepository).save(any(Baner.class));
        verifyNoInteractions(peliculaRepository);
    }

    @Test
    void updateBaner_siLaNuevaUrlYaExiste_lanzaConflicto() {
        Baner banerExistente = new Baner();
        banerExistente.setUrl("https://old/banner.jpg");

        BanerDTO cambios = new BanerDTO(
                UUID.randomUUID(),
                "https://other/banner.jpg",
                LocalDate.of(2026, 4, 2),
                LocalDate.of(2026, 4, 12)
        );

        when(banerRepository.findByUrl("https://old/banner.jpg")).thenReturn(Optional.of(banerExistente));
        when(banerRepository.existsByUrl("https://other/banner.jpg")).thenReturn(true);

        assertThatThrownBy(() -> banerService.updateBaner("https://old/banner.jpg", cambios))
                .isInstanceOf(EntityExistsException.class)
                .hasMessage("Ya existe un banner con la misma imagen.");

        verify(banerRepository).findByUrl("https://old/banner.jpg");
        verify(banerRepository).existsByUrl("https://other/banner.jpg");
        verify(banerRepository, never()).save(any());
        verifyNoInteractions(peliculaRepository);
    }

    @Test
    void updateBaner_siNoExisteLanzaNotFound() {
        BanerDTO cambios = new BanerDTO(
                UUID.randomUUID(),
                "https://new/banner.jpg",
                LocalDate.of(2026, 4, 2),
                LocalDate.of(2026, 4, 12)
        );

        when(banerRepository.findByUrl("https://old/banner.jpg")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> banerService.updateBaner("https://old/banner.jpg", cambios))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("No existe el baner a actualizar con la URL especificada.");

        verify(banerRepository).findByUrl("https://old/banner.jpg");
        verify(banerRepository, never()).existsByUrl(anyString());
        verify(banerRepository, never()).save(any());
        verifyNoInteractions(peliculaRepository);
    }
}
