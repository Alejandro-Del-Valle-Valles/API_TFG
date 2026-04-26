package com.tfg.API_TFG.core.repository;

import com.tfg.API_TFG.core.entity.BloqueoButaca;
import com.tfg.API_TFG.core.entity.id.SesionId;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BloqueoButacaRepository extends JpaRepository<BloqueoButaca, UUID> {

    @Modifying
    @Query("delete from BloqueoButaca b where b.expira < :now")
    int deleteExpired(@Param("now") LocalDateTime now);

    boolean existsBySesion_IdAndFilaAndButacaAndExpiraGreaterThanEqualAndTokenNot(SesionId sesionId, int fila, int butaca, LocalDateTime now, String token);

    Optional<BloqueoButaca> findBySesion_IdAndFilaAndButacaAndTokenAndExpiraGreaterThanEqual(SesionId sesionId, int fila, int butaca, String token, LocalDateTime now);

    List<BloqueoButaca> findBySesion_IdAndExpiraGreaterThanEqual(SesionId sesionId, LocalDateTime now);

    @Modifying
    int deleteBySesion_IdAndFilaAndButacaAndToken(SesionId sesionId, int fila, int butaca, String token);

    @Modifying
    int deleteBySesion_IdAndToken(SesionId sesionId, String token);

    @Modifying
    int deleteByToken(String token);
}

