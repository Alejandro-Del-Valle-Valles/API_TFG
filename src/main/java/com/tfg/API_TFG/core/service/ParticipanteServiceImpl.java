package com.tfg.API_TFG.core.service;

import com.tfg.API_TFG.adapter.ParticipanteAdapter;
import com.tfg.API_TFG.core.dto.ParticipanteDTO;
import com.tfg.API_TFG.core.entity.Participante;
import com.tfg.API_TFG.core.service.interfaces.ParticipanteService;
import com.tfg.API_TFG.core.repository.ParticipanteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParticipanteServiceImpl implements ParticipanteService {
    private final ParticipanteRepository participanteRepository;

    @Autowired
    public ParticipanteServiceImpl(ParticipanteRepository participanteRepository) {
        this.participanteRepository = participanteRepository;
    }

    @Override
    public List<ParticipanteDTO> getAll() {
        return participanteRepository.findAll().stream()
                .map(ParticipanteAdapter::toDTO)
                .toList();
    }

    @Override
    public List<ParticipanteDTO> getByNombre(String nombre) {
        return participanteRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(ParticipanteAdapter::toDTO)
                .toList();
    }

    @Override
    public ParticipanteDTO getById(Integer id) {
        return ParticipanteAdapter.toDTO(participanteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No existe el participante con ID " + id)));
    }

    @Override
    public ParticipanteDTO createParticipante(String nombre) {
        Participante participante = new Participante();
        participante.setNombre(nombre);
        return ParticipanteAdapter.toDTO(participanteRepository.save(participante));
    }

    @Override
    public ParticipanteDTO updateParticipante(ParticipanteDTO participanteDTO) {
        Optional<Participante> participanteExiste = participanteRepository.findById(participanteDTO.getId());
        if(participanteExiste.isEmpty()) throw new EntityNotFoundException("No existe el participante con ID " + participanteDTO.getId());
        Participante participante = participanteExiste.get();
        participante.setNombre(participanteDTO.getNombre());
        participanteRepository.save(participante);
        return participanteDTO;
    }

    @Override
    public ParticipanteDTO deleteParticipante(Integer id) {
        Optional<Participante> participanteExiste = participanteRepository.findById(id);
        if(participanteExiste.isEmpty()) throw new EntityNotFoundException("No existe el participante con ID " + id);
        Participante participante = participanteExiste.get();
        participanteRepository.delete(participante);
        return ParticipanteAdapter.toDTO(participante);
    }
}
