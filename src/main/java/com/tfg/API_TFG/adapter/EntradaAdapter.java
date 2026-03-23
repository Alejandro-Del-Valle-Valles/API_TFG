package com.tfg.API_TFG.adapter;

import com.tfg.API_TFG.core.dto.EntradaDTO;
import com.tfg.API_TFG.core.entity.Entrada;

public class EntradaAdapter {

    public static EntradaDTO toDTO(Entrada entrada) {
        return new EntradaDTO(SesionAdapter.toDTO(entrada.getSesion()),
                entrada.getPrecio());
    }
}
