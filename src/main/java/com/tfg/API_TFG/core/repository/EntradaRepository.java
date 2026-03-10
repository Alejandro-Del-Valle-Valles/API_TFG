package com.tfg.API_TFG.core.repository;

import com.tfg.API_TFG.core.entity.Entrada;
import com.tfg.API_TFG.core.entity.id.EntradaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntradaRepository extends JpaRepository<Entrada, EntradaId> {
}
