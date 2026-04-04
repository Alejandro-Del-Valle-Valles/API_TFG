package com.tfg.API_TFG.service;

import com.tfg.API_TFG.core.dto.ParticipanteCreateDTO;
import com.tfg.API_TFG.core.dto.ParticipanteDTO;
import com.tfg.API_TFG.core.dto.PeliculaCreateDTO;
import com.tfg.API_TFG.core.entity.Participante;
import com.tfg.API_TFG.core.entity.Pelicula;
import com.tfg.API_TFG.core.enums.GeneroPeliculas;
import com.tfg.API_TFG.core.enums.RolParticipante;
import com.tfg.API_TFG.core.repository.ParticipanteRepository;
import com.tfg.API_TFG.core.repository.PeliculaRepository;
import com.tfg.API_TFG.core.service.ParticipanteServiceImpl;
import com.tfg.API_TFG.core.service.PeliculaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PeliculaAndParticpanteService {

    @Mock
    PeliculaRepository peliculaRepository;

    @Mock
    ParticipanteRepository participanteRepository;

    @InjectMocks
    PeliculaServiceImpl peliculaService;

    @InjectMocks
    ParticipanteServiceImpl participanteService;

    @Test
    void debeCrearParticipante() {
        Participante toSave = new Participante();
        toSave.setNombre("Participante 1");

        Participante saved = new Participante();
        saved.setNombre("Participante 1");
        saved.setId(1);

        when(participanteRepository.save(any(Participante.class)))
                .thenReturn(saved);

        ParticipanteDTO result = participanteService.createParticipante("Participante 1");

        assertThat(result.getId()).isEqualTo(1);
        verify(participanteRepository).save(any(Participante.class));
    }

    @Test
    void debeCrearPeliculaCon2Participantes_unoConAmbosRoles_yOtroSoloActor() {
        PeliculaCreateDTO dto = new PeliculaCreateDTO(
                "Titanic",
                "Descripción",
                GeneroPeliculas.HISTORICA,
                "https://example.com/poster.jpg",
                LocalTime.of(3, 49),
                3,
                true,
                List.of(
                        new ParticipanteCreateDTO(1, List.of(RolParticipante.ACTOR, RolParticipante.DIRECTOR)),
                        new ParticipanteCreateDTO(2, List.of(RolParticipante.ACTOR))
                )
        );

        Participante p1 = new Participante();
        p1.setId(1);
        p1.setNombre("Participante 1");

        Participante p2 = new Participante();
        p2.setId(2);
        p2.setNombre("Participante 2");

        when(participanteRepository.findAllById(List.of(1, 2)))
                .thenReturn(List.of(p1, p2));

        UUID peliculaId = UUID.randomUUID();
        when(peliculaRepository.save(any(Pelicula.class))).thenAnswer(inv -> {
            Pelicula p = inv.getArgument(0);
            if (p.getId() == null) {
                p.setId(peliculaId);
            }
            return p;
        });

        peliculaService.createPelicula(dto);

        verify(participanteRepository).findAllById(List.of(1, 2));

        ArgumentCaptor<Pelicula> captor = ArgumentCaptor.forClass(Pelicula.class);
        verify(peliculaRepository, times(2)).save(captor.capture());

        List<Pelicula> saves = captor.getAllValues();
        Pelicula primerSave = saves.get(0);
        Pelicula segundoSave = saves.get(1);

        assertThat(primerSave.getNombre()).isEqualTo("Titanic");

        assertThat(segundoSave.getCreditos()).hasSize(3);

        assertThat(segundoSave.getCreditos())
                .extracting(c -> c.getParticipante().getId() + "|" + c.getRol().name())
                .containsExactlyInAnyOrder(
                        "1|ACTOR",
                        "1|DIRECTOR",
                        "2|ACTOR"
                );

        verifyNoMoreInteractions(participanteRepository, peliculaRepository);
    }
}
