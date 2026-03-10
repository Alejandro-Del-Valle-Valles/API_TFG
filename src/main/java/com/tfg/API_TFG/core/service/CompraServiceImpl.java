package com.tfg.API_TFG.core.service;

import com.tfg.API_TFG.core.service.interfaces.CompraService;
import com.tfg.API_TFG.core.repository.CompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompraServiceImpl implements CompraService {
    private final CompraRepository compraRepository;

    @Autowired
    public CompraServiceImpl(CompraRepository compraRepository) {
        this.compraRepository = compraRepository;
    }
}
