package com.tfg.API_TFG.core.service;

import com.tfg.API_TFG.core.service.interfaces.ProyeccionService;
import com.tfg.API_TFG.core.repository.ProyeccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProyeccionServiceImpl implements ProyeccionService {


    private final ProyeccionRepository proyeccionRepository;

    @Autowired
    public ProyeccionServiceImpl(ProyeccionRepository proyeccionRepository) {
        this.proyeccionRepository = proyeccionRepository;
    }
}
