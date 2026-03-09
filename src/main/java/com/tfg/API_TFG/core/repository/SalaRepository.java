package com.tfg.API_TFG.core.repository;

import com.tfg.API_TFG.core.entity.Sala;
import com.tfg.API_TFG.core.entity.id.SalaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaRepository extends JpaRepository<Sala, SalaId> {
}
