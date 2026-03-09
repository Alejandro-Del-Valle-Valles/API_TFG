package com.tfg.API_TFG.core.repository;

import com.tfg.API_TFG.core.entity.Proyeccion;
import com.tfg.API_TFG.core.entity.id.ProyeccionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProyeccionRepository extends JpaRepository<Proyeccion, ProyeccionId> {
}
