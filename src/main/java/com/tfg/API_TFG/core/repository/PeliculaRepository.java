package com.tfg.API_TFG.core.repository;

import com.tfg.API_TFG.core.entity.Pelicula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PeliculaRepository extends JpaRepository<Pelicula, UUID>{
    List<Pelicula> findByNombreContainingIgnoreCase(String nombre);
}
