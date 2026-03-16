package com.tfg.API_TFG.core.controller;

import com.tfg.API_TFG.core.service.interfaces.SesionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sesion")
@Tag(name = "Sesiones", description = "API para las sesiones")
public class SesionController {
    private final SesionService sesionService;

    @Autowired
    public SesionController(SesionService sesionService) {
        this.sesionService = sesionService;
    }
}
