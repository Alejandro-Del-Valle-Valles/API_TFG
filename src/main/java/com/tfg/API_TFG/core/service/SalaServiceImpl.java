package com.tfg.API_TFG.core.service;

import com.tfg.API_TFG.core.service.interfaces.SalaService;
import com.tfg.API_TFG.core.repository.SalaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SalaServiceImpl implements SalaService {


    private final SalaRepository salaRepository;

    @Autowired
    public SalaServiceImpl(SalaRepository salaRepository) {
        this.salaRepository = salaRepository;
    }
}
