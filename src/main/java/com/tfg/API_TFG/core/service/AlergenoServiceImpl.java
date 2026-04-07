package com.tfg.API_TFG.core.service;

import com.tfg.API_TFG.adapter.AlergenoAdapter;
import com.tfg.API_TFG.core.dto.AlergenoDTO;
import com.tfg.API_TFG.core.entity.Alergeno;
import com.tfg.API_TFG.core.service.interfaces.AlergenoService;
import com.tfg.API_TFG.core.repository.AlergenoRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlergenoServiceImpl implements AlergenoService {

    private final AlergenoRepository alergenoRepository;

    @Autowired
    public AlergenoServiceImpl(AlergenoRepository alergenoRepository) {
        this.alergenoRepository = alergenoRepository;
    }

    @Override
    public List<AlergenoDTO> getAll() {
        return alergenoRepository.findAll().stream()
                .map(AlergenoAdapter::toDTO)
                .toList();
    }

    @Override
    public AlergenoDTO getAlergenoByNombre(String nombre) {
        return AlergenoAdapter.toDTO(alergenoRepository.findByNombreIgnoreCase(nombre)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el alérgeno con nombre " + nombre)));
    }

    @Override
    public AlergenoDTO createAlergeno(@Valid AlergenoDTO alergenoDTO) {
        Optional<Alergeno> alergenoExiste = alergenoRepository.findByNombreIgnoreCase(alergenoDTO.nombre());
        if(alergenoExiste.isPresent()) throw new EntityExistsException("Ya existe un alérgeno con el nombre " +  alergenoDTO.nombre());
        Alergeno alergeno = new Alergeno();
        alergeno.setNombre(alergenoDTO.nombre());
        alergenoRepository.save(alergeno);
        return alergenoDTO;
    }

    @Override
    public AlergenoDTO updateAlergeno(String nombreAntiguo, @Valid AlergenoDTO alergenoDTO) {
        Optional<Alergeno> alergenoExiste = alergenoRepository.findByNombreIgnoreCase(alergenoDTO.nombre());
        if(alergenoExiste.isPresent()) throw new EntityExistsException("Ya existe un alérgeno con el nombre " +  alergenoDTO.nombre());
        Alergeno alergeno = alergenoRepository.findByNombreIgnoreCase(nombreAntiguo)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el alérgeno con nombre " + nombreAntiguo));
        alergeno.setNombre(alergenoDTO.nombre());
        alergenoRepository.save(alergeno);
        return alergenoDTO;
    }

    @Override
    public AlergenoDTO deleteAlergeno(String nombre) {
        Alergeno alergeno = alergenoRepository.findByNombreIgnoreCase(nombre)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el alérgeno con nombre " + nombre));
        alergenoRepository.delete(alergeno);
        return new AlergenoDTO(nombre);
    }
}
