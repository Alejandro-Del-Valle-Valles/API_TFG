package com.tfg.API_TFG.core.repository;

import com.tfg.API_TFG.core.entity.Sesion;
import com.tfg.API_TFG.core.entity.id.SesionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SesionRepository extends JpaRepository<Sesion, SesionId> {
}
