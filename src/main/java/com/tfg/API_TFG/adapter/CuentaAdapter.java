package com.tfg.API_TFG.adapter;

import com.tfg.API_TFG.core.dto.CuentaDTO;
import com.tfg.API_TFG.core.entity.Cuenta;

public class CuentaAdapter {

    public static CuentaDTO toDTO(Cuenta cuenta) {
        return new CuentaDTO(cuenta.getUsuario().getCorreo(), cuenta.getNombre(), cuenta.getContrasena(),
                cuenta.getRol());
    }
}
