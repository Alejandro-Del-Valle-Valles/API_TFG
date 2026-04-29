package com.tfg.API_TFG.core.repository;

import com.tfg.API_TFG.core.entity.Sesion;
import com.tfg.API_TFG.core.entity.id.SesionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface SesionRepository extends JpaRepository<Sesion, SesionId> {

    List<Sesion> findByIdHorarioSesionBetween(LocalDateTime horarioInicio, LocalDateTime horarioFin);
    List<Sesion> findByIdHorarioSesionAndSalaNumSala(LocalDateTime horario, Integer numSala);
    List<Sesion> findByPeliculaId(UUID peliculaId);
    List<Sesion> findByPeliculaIdAndIdHorarioSesionAfter(UUID peliculaId, LocalDateTime ayer);

}
