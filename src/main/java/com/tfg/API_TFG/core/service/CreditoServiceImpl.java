package com.tfg.API_TFG.core.service;

import com.tfg.API_TFG.core.service.interfaces.CreditoService;
import com.tfg.API_TFG.core.repository.CreditoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreditoServiceImpl implements CreditoService {
    private final CreditoRepository creditoRepository;

    @Autowired
    public CreditoServiceImpl(CreditoRepository creditoRepository) {
        this.creditoRepository = creditoRepository;
    }
}
