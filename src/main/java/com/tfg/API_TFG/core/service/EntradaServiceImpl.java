package com.tfg.API_TFG.core.service;

import com.tfg.API_TFG.core.service.interfaces.EntradaService;
import com.tfg.API_TFG.core.repository.EntradaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EntradaServiceImpl implements EntradaService {
    private final EntradaRepository entradaRepository;

    @Autowired
    public EntradaServiceImpl(EntradaRepository entradaRepository) {
        this.entradaRepository = entradaRepository;
    }
}
