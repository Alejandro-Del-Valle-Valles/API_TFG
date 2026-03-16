package com.tfg.API_TFG.core.controller;

import com.tfg.API_TFG.core.service.interfaces.ProyeccionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/proyeccion")
@Tag(name = "Proyecciones", description = "API para las proyecciones")
public class ProyeccionController {

    private final ProyeccionService proyeccionService;

    @Autowired
    public ProyeccionController(ProyeccionService proyeccionService) {
        this.proyeccionService = proyeccionService;
    }
}
