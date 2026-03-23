package com.tfg.API_TFG.adapter;

import com.tfg.API_TFG.core.dto.LineaCompraDTO;
import com.tfg.API_TFG.core.dto.LineaCompraEntradaDTO;
import com.tfg.API_TFG.core.dto.LineaCompraProductoDTO;
import com.tfg.API_TFG.core.entity.LineaCompra;

public class LineaCompraAdapter {

    public static LineaCompraDTO toDTO(LineaCompra lineaCompra) {
        if(lineaCompra.getProducto() != null) {
            return new LineaCompraProductoDTO(lineaCompra.getId().getNumLinea(),
                    ProductoAdapter.toDTO(lineaCompra.getProducto()));
        } else {
            return new LineaCompraEntradaDTO(lineaCompra.getId().getNumLinea(),
                    EntradaAdapter.toDTO(lineaCompra.getEntrada()));
        }
    }
}
