package com.tfg.API_TFG.core.service.interfaces;

import com.tfg.API_TFG.core.dto.AlergenoDTO;

import java.util.List;

public interface AlergenoService {

    List<AlergenoDTO> getAll();
    AlergenoDTO getAlergenoByNombre(String nombre);
    AlergenoDTO createAlergeno(AlergenoDTO alergenoDTO);
    AlergenoDTO updateAlergeno(String nombreAntiguo, AlergenoDTO alergenoDTO);
    AlergenoDTO deleteAlergeno(String nombre);
}
