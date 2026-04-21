package com.tfg.API_TFG.core.service;

import com.tfg.API_TFG.adapter.SalaAdapter;
import com.tfg.API_TFG.core.dto.SalaDTO;
import com.tfg.API_TFG.core.entity.Sala;
import com.tfg.API_TFG.core.service.interfaces.SalaService;
import com.tfg.API_TFG.core.repository.SalaRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SalaServiceImpl implements SalaService {

    private final SalaRepository salaRepository;

    @Autowired
    public SalaServiceImpl(SalaRepository salaRepository) {
        this.salaRepository = salaRepository;
    }

    @Override
    public List<SalaDTO> getAll() {
        return salaRepository.findAll().stream()
                .map(SalaAdapter::toDTO)
                .toList();
    }

    @Override
    public SalaDTO getSala(Integer numero) {
        return SalaAdapter.toDTO(salaRepository.findById(numero)
                .orElseThrow(() -> new EntityNotFoundException("No existe la sala " + numero)));
    }

    @Override
    public SalaDTO createSala(SalaDTO salaDTO) {
        Optional<Sala> sala = salaRepository.findById(salaDTO.numero());
        if(sala.isPresent()) throw new EntityExistsException("Ya existe la sala " + salaDTO.numero());
        Sala nuevaSala = new Sala();
        nuevaSala.setNumSala(salaDTO.numero());
        nuevaSala.setAforo(salaDTO.aforo());
        salaRepository.save(nuevaSala);
        return salaDTO;
    }

    @Override
    public SalaDTO updateSala(SalaDTO salaDTO) {
        Sala sala = salaRepository.findById(salaDTO.numero())
                .orElseThrow(() -> new EntityNotFoundException("No existe la sala " + salaDTO.numero()));
        sala.setAforo(salaDTO.aforo());
        salaRepository.save(sala);
        return salaDTO;
    }

    @Override
    public SalaDTO deleteSala(Integer numero) {
        Sala sala = salaRepository.findById(numero)
                .orElseThrow(() -> new EntityNotFoundException("No existe la sala " + numero));
        salaRepository.delete(sala);
        return SalaAdapter.toDTO(sala);
    }
}
