package com.tfg.API_TFG.core.controller;

import com.tfg.API_TFG.core.service.interfaces.EntradaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/entrada")
@Tag(name = "Entradas", description = "API para las entradas")
public class EntradaController {

    private final EntradaService entradaService;

    @Autowired
    public EntradaController(EntradaService entradaService) {
        this.entradaService = entradaService;
    }
}
