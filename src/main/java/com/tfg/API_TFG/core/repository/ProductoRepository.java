package com.tfg.API_TFG.core.repository;

import com.tfg.API_TFG.core.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    Optional<Producto> findByNombreIgnoreCase(String nombre);
}
