package com.tfg.API_TFG.core.service.interfaces;

import com.tfg.API_TFG.core.dto.CuentaDTO;
import com.tfg.API_TFG.core.dto.CuentaLoginDTO;
import com.tfg.API_TFG.core.dto.CuentaUpdateDTO;
import com.tfg.API_TFG.core.dto.LoginDTO;

import javax.naming.AuthenticationException;

public interface CuentaService {
    CuentaLoginDTO login(LoginDTO login) throws AuthenticationException;
    CuentaDTO createCuenta(CuentaDTO cuentaDTO);
    CuentaDTO updateCuenta(String correo, CuentaUpdateDTO updateDTO) throws AuthenticationException;
    CuentaDTO deleteCuenta(String correo);
}
