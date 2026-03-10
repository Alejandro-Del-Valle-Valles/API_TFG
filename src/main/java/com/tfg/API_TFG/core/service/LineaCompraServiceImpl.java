package com.tfg.API_TFG.core.service;

import com.tfg.API_TFG.core.service.interfaces.LineaCompraService;
import com.tfg.API_TFG.core.repository.LineaCompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LineaCompraServiceImpl implements LineaCompraService {
    private final LineaCompraRepository lineaCompraRepository;

    @Autowired
    public LineaCompraServiceImpl(LineaCompraRepository lineaCompraRepository) {
        this.lineaCompraRepository = lineaCompraRepository;
    }
}
