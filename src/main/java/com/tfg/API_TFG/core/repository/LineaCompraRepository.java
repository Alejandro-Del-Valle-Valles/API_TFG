package com.tfg.API_TFG.core.repository;

import com.tfg.API_TFG.core.entity.LineaCompra;
import com.tfg.API_TFG.core.entity.id.LineaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineaCompraRepository extends JpaRepository<LineaCompra, LineaId> {
}
