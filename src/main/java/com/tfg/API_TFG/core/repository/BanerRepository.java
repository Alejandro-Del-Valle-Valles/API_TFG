package com.tfg.API_TFG.core.repository;

import com.tfg.API_TFG.core.entity.Baner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BanerRepository extends JpaRepository<Baner, UUID> {
    List<Baner> findByEmpiezaLessThanEqualAndTerminaGreaterThanEqual(LocalDate empieza, LocalDate termina);
    Optional<Baner> findByUrl(String url);
    boolean existsByUrl(String url);
}
