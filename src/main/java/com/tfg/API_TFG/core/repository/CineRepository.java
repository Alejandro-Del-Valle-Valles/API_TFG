package com.tfg.API_TFG.core.repository;

import com.tfg.API_TFG.core.entity.Cine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CineRepository extends JpaRepository<Cine, Integer> {
}
