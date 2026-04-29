package com.tfg.API_TFG.core.service.interfaces;

import com.tfg.API_TFG.core.dto.CreateTipoEntradaDTO;
import com.tfg.API_TFG.core.dto.TipoEntradaDTO;

import java.util.List;

public interface TipoEntradaService {
    List<TipoEntradaDTO> getAll();
    TipoEntradaDTO getById(int id);
    TipoEntradaDTO createTipoEntrada(CreateTipoEntradaDTO createTipoEntradaDTO);
    TipoEntradaDTO updateTipoEntrada(TipoEntradaDTO tipoEntradaDTO);
    TipoEntradaDTO deleteTipoEntrada(int id);
}
