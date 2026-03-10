package com.tfg.API_TFG.core.service;

import com.tfg.API_TFG.core.service.interfaces.AlergenoService;
import com.tfg.API_TFG.core.repository.AlergenoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlergenoServiceImpl implements AlergenoService {


    private final AlergenoRepository alergenoRepository;

    @Autowired
    public AlergenoServiceImpl(AlergenoRepository alergenoRepository) {
        this.alergenoRepository = alergenoRepository;
    }
}
