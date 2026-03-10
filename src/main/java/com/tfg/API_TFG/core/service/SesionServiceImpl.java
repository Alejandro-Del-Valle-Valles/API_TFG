package com.tfg.API_TFG.core.service;

import com.tfg.API_TFG.core.service.interfaces.SesionService;
import com.tfg.API_TFG.core.repository.SesionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SesionServiceImpl implements SesionService {
    private final SesionRepository sesionRepository;

    @Autowired
    public SesionServiceImpl(SesionRepository sesionRepository) {
        this.sesionRepository = sesionRepository;
    }
}
