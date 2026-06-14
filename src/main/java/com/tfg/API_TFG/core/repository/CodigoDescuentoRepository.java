package com.tfg.API_TFG.core.repository;

import com.tfg.API_TFG.core.entity.CodigoDescuento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodigoDescuentoRepository extends JpaRepository<CodigoDescuento, Integer> {

    Optional<CodigoDescuento> findByCodigo(String codigo);

    boolean existsByCodigo(String codigoDescuento);
}
