package com.tfg.API_TFG.core.service;

import com.tfg.API_TFG.adapter.BanerAdapter;
import com.tfg.API_TFG.core.dto.BanerDTO;
import com.tfg.API_TFG.core.entity.Baner;
import com.tfg.API_TFG.core.entity.Pelicula;
import com.tfg.API_TFG.core.repository.BanerRepository;
import com.tfg.API_TFG.core.repository.PeliculaRepository;
import com.tfg.API_TFG.core.service.interfaces.BanerService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BanerServiceImpl implements BanerService {

    private final BanerRepository banerRepository;
    private final PeliculaRepository peliculaRepository;

    @Autowired
    public BanerServiceImpl(BanerRepository banerRepository, PeliculaRepository peliculaRepository) {
        this.banerRepository = banerRepository;
        this.peliculaRepository = peliculaRepository;
    }

    @Override
    public List<BanerDTO> getTodayBaners() {
        LocalDate hoy = LocalDate.now();
        return banerRepository.findByEmpiezaLessThanEqualAndTerminaGreaterThanEqual(hoy, hoy).stream()
                .map(BanerAdapter::toDTO)
                .toList();
    }

    @Override
    public BanerDTO createBaner(BanerDTO banerDTO) {
        if(banerRepository.existsByUrl(banerDTO.url())) throw new EntityExistsException("Ya existe un banner con la misma imagen.");
        Pelicula pelicula = peliculaRepository.findById(banerDTO.peliculaId()).orElseThrow(
                () -> new EntityNotFoundException("No existe la película con el ID especificado")
        );
        Baner baner = new Baner();
        baner.setUrl(banerDTO.url());
        baner.setEmpieza(banerDTO.empieza());
        baner.setTermina(banerDTO.termina());
        pelicula.addBaner(baner);
        return BanerAdapter.toDTO(banerRepository.save(baner));
    }

    @Override
    public BanerDTO updateBaner(String antiguaUrl, BanerDTO banerDTO) {
        if(banerRepository.existsByUrl(banerDTO.url())) throw new EntityExistsException("Ya existe un banner con la misma imagen.");
        Baner baner = banerRepository.findByUrl(antiguaUrl).orElseThrow(
                () -> new EntityNotFoundException("No existe el baner a actualizar con la URL especificada.")
        );
        Pelicula pelicula = peliculaRepository.findById(banerDTO.peliculaId()).orElseThrow(
                () -> new EntityNotFoundException("No existe la película con el ID especificado")
        );
        baner.setUrl(banerDTO.url());
        baner.setEmpieza(banerDTO.empieza());
        baner.setTermina(banerDTO.termina());
        pelicula.addBaner(baner);
        return BanerAdapter.toDTO(banerRepository.save(baner));
    }

    @Override
    public BanerDTO deleteBaner(String url) {
        Baner baner = banerRepository.findByUrl(url).orElseThrow(
                () -> new EntityNotFoundException("No existe el banner con la url especificada")
        );
        banerRepository.delete(baner);
        return BanerAdapter.toDTO(baner);
    }
}
