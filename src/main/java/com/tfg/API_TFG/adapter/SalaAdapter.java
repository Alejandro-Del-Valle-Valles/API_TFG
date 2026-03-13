package com.tfg.API_TFG.adapter;

import com.tfg.API_TFG.core.dto.SalaDTO;
import com.tfg.API_TFG.core.entity.Sala;

public class SalaAdapter {

    public static SalaDTO toDTO(Sala sala) {
        return new SalaDTO(sala.getNumSala(), sala.getAforo());
    }
}
