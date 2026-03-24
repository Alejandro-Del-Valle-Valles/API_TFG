package com.tfg.API_TFG.adapter;

import com.tfg.API_TFG.core.dto.CompraDTO;
import com.tfg.API_TFG.core.dto.LineaCompraDTO;
import com.tfg.API_TFG.core.entity.Compra;

import java.util.List;

public class CompraAdapter {

    public static CompraDTO toDTO(Compra compra) {
        List<LineaCompraDTO> lineas = compra.getLineaCompras().stream()
                .map(LineaCompraAdapter::toDTO)
                .toList();
        return new CompraDTO(compra.getUsuario().getCorreo(), compra.getFecha(),
                lineas);
    }
}
