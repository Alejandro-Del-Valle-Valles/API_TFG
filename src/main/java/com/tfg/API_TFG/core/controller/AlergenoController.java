package com.tfg.API_TFG.core.controller;

import com.tfg.API_TFG.core.service.interfaces.AlergenoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alergeno")
public class AlergenoController {

    private final AlergenoService alergenoService;

    @Autowired
    public AlergenoController(AlergenoService alergenoService) {
        this.alergenoService = alergenoService;
    }
}
