package com.tfg.API_TFG.adapter;

import com.tfg.API_TFG.core.dto.TipoEntradaDTO;
import com.tfg.API_TFG.core.entity.TipoEntrada;

public class TipoEntradaAdapter {

    public static TipoEntradaDTO toDTO(TipoEntrada tipoEntrada) {
        return new TipoEntradaDTO(tipoEntrada.getId(), tipoEntrada.getNombre(),
                tipoEntrada.getDescripcion(), tipoEntrada.getPrecio().floatValue());
    }
}
