package com.tfg.API_TFG.core.service;

import com.tfg.API_TFG.core.service.interfaces.ParticipanteService;
import com.tfg.API_TFG.core.repository.ParticipanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParticipanteServiceImpl implements ParticipanteService {
    private final ParticipanteRepository participanteRepository;

    @Autowired
    public ParticipanteServiceImpl(ParticipanteRepository participanteRepository) {
        this.participanteRepository = participanteRepository;
    }
}
