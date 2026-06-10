package com.tfg.API_TFG.core.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    /**
     * Endpoint para que se llame cada pocos minutos y la API se mantenga despierta
     * @return String
     */
    @GetMapping("/ping")
    public String ping() {
        return "Servicio despierto";
    }
}
