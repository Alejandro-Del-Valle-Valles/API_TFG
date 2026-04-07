package com.tfg.API_TFG.core.repository;

import com.tfg.API_TFG.core.entity.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, UUID> {
    Optional<Cuenta> findByUsuarioCorreo(String correo);
    boolean existsByUsuarioCorreo(String correo);
}
