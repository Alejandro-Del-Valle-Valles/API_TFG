package com.tfg.API_TFG.core.service.interfaces;

import com.tfg.API_TFG.core.dto.SalaDTO;

import java.util.List;

public interface SalaService {

    List<SalaDTO> getAll();
    SalaDTO getSala(Integer numero);
    SalaDTO createSala(SalaDTO salaDTO);
    SalaDTO updateSala(SalaDTO salaDTO);
    SalaDTO deleteSala(Integer numero);
}
