package com.tfg.API_TFG.core.controller;

import com.tfg.API_TFG.core.service.interfaces.ProyeccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/proyeccion")
public class ProyeccionController {

    private final ProyeccionService proyeccionService;

    @Autowired
    public ProyeccionController(ProyeccionService proyeccionService) {
        this.proyeccionService = proyeccionService;
    }
}
