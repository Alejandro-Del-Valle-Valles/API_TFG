package com.tfg.API_TFG.core.service.interfaces;

import com.tfg.API_TFG.core.dto.CodigoDescuentoCreateDTO;
import com.tfg.API_TFG.core.dto.CodigoDescuentoDTO;

import java.util.List;

public interface CodigoDescuentoService {

    List<CodigoDescuentoDTO> getAll();

    CodigoDescuentoDTO getById(Integer id);

    CodigoDescuentoDTO getByCodigo(String codigo);

    CodigoDescuentoDTO createCodigoDescuento(
            CodigoDescuentoCreateDTO dto
    );

    CodigoDescuentoDTO updateCodigoDescuento(
            Integer id,
            CodigoDescuentoCreateDTO dto
    );
}
