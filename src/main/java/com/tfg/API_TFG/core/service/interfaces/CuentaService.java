package com.tfg.API_TFG.core.service.interfaces;

import com.tfg.API_TFG.core.dto.CuentaDTO;

public interface CuentaService {
    CuentaDTO getByCorreo(String correo);
    CuentaDTO createCuenta(CuentaDTO cuentaDTO);
    CuentaDTO updateCuenta(CuentaDTO cuentaDTO);
    CuentaDTO deleteCuenta(String correo);
}
