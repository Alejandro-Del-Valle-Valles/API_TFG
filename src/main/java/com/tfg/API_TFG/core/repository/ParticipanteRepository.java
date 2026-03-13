package com.tfg.API_TFG.core.repository;

import com.tfg.API_TFG.core.entity.Participante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipanteRepository extends JpaRepository<Participante, Integer> {
    List<Participante> findByNombreContainingIgnoreCase(String nombre);
}
