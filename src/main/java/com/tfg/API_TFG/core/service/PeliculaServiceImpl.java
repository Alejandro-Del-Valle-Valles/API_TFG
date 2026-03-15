package com.tfg.API_TFG.core.service;

import com.tfg.API_TFG.adapter.PeliculaAdapter;
import com.tfg.API_TFG.core.dto.ParticipanteCompletoDTO;
import com.tfg.API_TFG.core.dto.PeliculaCompletoDTO;
import com.tfg.API_TFG.core.dto.PeliculaCreteDTO;
import com.tfg.API_TFG.core.dto.PeliculaDTO;
import com.tfg.API_TFG.core.entity.Credito;
import com.tfg.API_TFG.core.entity.Participante;
import com.tfg.API_TFG.core.entity.Pelicula;
import com.tfg.API_TFG.core.repository.CreditoRepository;
import com.tfg.API_TFG.core.repository.ParticipanteRepository;
import com.tfg.API_TFG.core.service.interfaces.PeliculaService;
import com.tfg.API_TFG.core.repository.PeliculaRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PeliculaServiceImpl implements PeliculaService {
    private final PeliculaRepository peliculaRepository;
    private final CreditoRepository creditoRepository;
    private final ParticipanteRepository participanteRepository;

    @Autowired
    public PeliculaServiceImpl(PeliculaRepository peliculaRepository, CreditoRepository creditoRepository,
                               ParticipanteRepository participanteRepository) {
        this.peliculaRepository = peliculaRepository;
        this.creditoRepository = creditoRepository;
        this.participanteRepository = participanteRepository;
    }

    @Override
    public List<PeliculaDTO> getAll() {
        return peliculaRepository.findAll().stream()
                .map(PeliculaAdapter::toDTO)
                .toList();
    }

    @Override
    public List<PeliculaCompletoDTO> getAllCompleto() {
        return peliculaRepository.findAll().stream()
                .map(PeliculaAdapter::toCompletoDTO)
                .toList();
    }

    @Override
    public List<PeliculaDTO> getByNombreContainingIgonreCase(String nombre) {
        return peliculaRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(PeliculaAdapter::toDTO)
                .toList();
    }

    @Override
    public List<PeliculaCompletoDTO> getCompletoByNombreContainingIgnoreCase(String nombre) {
        return peliculaRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(PeliculaAdapter::toCompletoDTO)
                .toList();
    }

    @Override
    public PeliculaCompletoDTO createPelicula(PeliculaCreteDTO peliculaCreateDTO) {
        Pelicula pelicula = new Pelicula();
        pelicula.setNombre(peliculaCreateDTO.getNombre());
        pelicula.setDescripcion(peliculaCreateDTO.getDescripcion());
        pelicula.setDuracion(peliculaCreateDTO.getDuracion());
        pelicula.setPortada(pelicula.getPortada());
        pelicula.setCalificacionEdad(pelicula.getCalificacionEdad());
        for(ParticipanteCompletoDTO p : peliculaCreateDTO.getParticipantes()) {
            Optional<Participante> participanteExiste = participanteRepository.findById(p.getId());
            if(participanteExiste.isEmpty()) throw new EntityExistsException("No existe el participante con ID " + p.getId());

        }
        return null;
    }

    @Override
    public PeliculaCompletoDTO updatePeliculaCompleto(PeliculaCompletoDTO peliculaCompletoDTO) {
        return null;
    }

    @Override
    public PeliculaDTO deletePelicula(UUID id) {
        return null;
    }
}
