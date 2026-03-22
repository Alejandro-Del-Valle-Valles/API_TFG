package com.tfg.API_TFG.adapter;

import com.tfg.API_TFG.core.dto.UsuarioDTO;
import com.tfg.API_TFG.core.entity.Usuario;

public class UsuarioAdapter {

    public static UsuarioDTO toDTO(Usuario usuario) {
        return new UsuarioDTO(usuario.getCorreo());
    }
}
