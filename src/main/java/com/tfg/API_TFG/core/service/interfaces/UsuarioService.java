package com.tfg.API_TFG.core.service.interfaces;

import com.tfg.API_TFG.core.dto.UsuarioDTO;

public interface UsuarioService {
    UsuarioDTO getUsuarioByCorreo(String correo);
    UsuarioDTO createUsuario(UsuarioDTO usuarioDTO);
}
