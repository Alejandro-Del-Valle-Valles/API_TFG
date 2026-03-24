package com.tfg.API_TFG.core.service.interfaces;

import com.tfg.API_TFG.core.dto.CompraDTO;

import java.util.List;

public interface CompraService {

    List<CompraDTO> getAllByCorreoUsuario(String correo);
    CompraDTO createCompra(CompraDTO compraDTO);
    
}
