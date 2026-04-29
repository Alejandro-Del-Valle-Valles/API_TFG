package com.tfg.API_TFG.core.service;

import com.tfg.API_TFG.adapter.TipoEntradaAdapter;
import com.tfg.API_TFG.core.dto.CreateTipoEntradaDTO;
import com.tfg.API_TFG.core.dto.TipoEntradaDTO;
import com.tfg.API_TFG.core.entity.TipoEntrada;
import com.tfg.API_TFG.core.repository.TipoEntradaRepository;
import com.tfg.API_TFG.core.service.interfaces.TipoEntradaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TipoEntradaServiceImpl implements TipoEntradaService {

    private final TipoEntradaRepository tipoEntradaRepository;

    @Autowired
    public TipoEntradaServiceImpl(TipoEntradaRepository tipoEntradaRepository) {
        this.tipoEntradaRepository = tipoEntradaRepository;
    }

    @Override
    public List<TipoEntradaDTO> getAll() {
        return tipoEntradaRepository.findAll().stream()
                .map(TipoEntradaAdapter::toDTO)
                .toList();
    }

    @Override
    public TipoEntradaDTO getById(int id) {
        return tipoEntradaRepository.findById(id)
                .map(TipoEntradaAdapter::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de entrada con ID " + id + " no encontrado."));
    }

    @Override
    public TipoEntradaDTO createTipoEntrada(CreateTipoEntradaDTO createTipoEntradaDTO) {
        TipoEntrada tipoEntrada = new TipoEntrada();
        tipoEntrada.setNombre(createTipoEntradaDTO.nombre());
        tipoEntrada.setDescripcion(createTipoEntradaDTO.descripcion());
        tipoEntrada.setPrecio(new BigDecimal(createTipoEntradaDTO.precio()));
        return TipoEntradaAdapter.toDTO(tipoEntradaRepository.save(tipoEntrada));
    }

    @Override
    public TipoEntradaDTO updateTipoEntrada(TipoEntradaDTO tipoEntradaDTO) {
        TipoEntrada tipoEntrada = tipoEntradaRepository.findById(tipoEntradaDTO.id())
                        .orElseThrow(() -> new EntityNotFoundException("Tipo de entrada con ID " + tipoEntradaDTO.id() + " no encontrado."));
        tipoEntrada.setNombre(tipoEntradaDTO.nombre());
        tipoEntrada.setDescripcion(tipoEntradaDTO.descripcion());
        tipoEntrada.setPrecio(new BigDecimal(tipoEntradaDTO.precio()));
        return TipoEntradaAdapter.toDTO(tipoEntradaRepository.save(tipoEntrada));
    }

    @Override
    public TipoEntradaDTO deleteTipoEntrada(int id) {
        TipoEntrada tipoEntrada = tipoEntradaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de entrada con ID " + id + " no encontrado."));
        tipoEntradaRepository.delete(tipoEntrada);
        return TipoEntradaAdapter.toDTO(tipoEntrada);
    }
}
