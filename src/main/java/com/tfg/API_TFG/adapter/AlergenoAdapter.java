package com.tfg.API_TFG.adapter;

import com.tfg.API_TFG.core.dto.AlergenoDTO;
import com.tfg.API_TFG.core.entity.Alergeno;

public class AlergenoAdapter {

    public static AlergenoDTO toDTO(Alergeno alergeno) {
        return new AlergenoDTO(alergeno.getNombre());
    }
}
