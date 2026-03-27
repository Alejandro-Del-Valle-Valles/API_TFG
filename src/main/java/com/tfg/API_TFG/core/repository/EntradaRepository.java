package com.tfg.API_TFG.core.repository;

import com.tfg.API_TFG.core.entity.Entrada;
import com.tfg.API_TFG.core.entity.id.EntradaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EntradaRepository extends JpaRepository<Entrada, EntradaId> {
    List<Entrada> findDistinctByLineaCompraCompraUsuarioCorreoAndSesionHorarioAfter(String correo, LocalDateTime fecha);
}
