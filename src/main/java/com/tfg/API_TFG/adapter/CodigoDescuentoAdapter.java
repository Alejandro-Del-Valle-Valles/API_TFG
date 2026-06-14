package com.tfg.API_TFG.adapter;

import com.tfg.API_TFG.core.dto.CodigoDescuentoDTO;
import com.tfg.API_TFG.core.entity.CodigoDescuento;

public class CodigoDescuentoAdapter {

    public static CodigoDescuentoDTO toDTO(CodigoDescuento codigoDescuento) {
        return new CodigoDescuentoDTO(
                codigoDescuento.getId(),
                codigoDescuento.getCodigo(),
                codigoDescuento.getCondicion(),
                codigoDescuento.getPorcentajeDescuento(),
                codigoDescuento.isActivo()
        );
    }
}
