package com.tfg.API_TFG.core.controller;

import com.tfg.API_TFG.core.service.interfaces.LineaCompraService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/linea-compra")
@Tag(name = "Líneas de Compra", description = "API para las líneas de compra")
public class LineaCompraController {

    private final LineaCompraService lineaCompraService;

    @Autowired
    public LineaCompraController(LineaCompraService lineaCompraService) {
        this.lineaCompraService = lineaCompraService;
    }
}
