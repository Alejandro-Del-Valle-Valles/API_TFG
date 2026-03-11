package com.tfg.API_TFG.core.repository;

import com.tfg.API_TFG.core.entity.Alergeno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlergenoRepository extends JpaRepository<Alergeno, Integer> {
    Optional<Alergeno> findByNombreIgnoreCase(String nombre);
}
