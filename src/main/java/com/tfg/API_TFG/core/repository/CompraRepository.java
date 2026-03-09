package com.tfg.API_TFG.core.repository;

import com.tfg.API_TFG.core.entity.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CompraRepository extends JpaRepository<Compra, UUID> {
}
