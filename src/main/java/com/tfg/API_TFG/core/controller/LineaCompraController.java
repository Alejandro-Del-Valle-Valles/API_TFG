package com.tfg.API_TFG.core.controller;

import com.tfg.API_TFG.core.service.interfaces.LineaCompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/linea-compra")
public class LineaCompraController {

    private final LineaCompraService lineaCompraService;

    @Autowired
    public LineaCompraController(LineaCompraService lineaCompraService) {
        this.lineaCompraService = lineaCompraService;
    }
}
