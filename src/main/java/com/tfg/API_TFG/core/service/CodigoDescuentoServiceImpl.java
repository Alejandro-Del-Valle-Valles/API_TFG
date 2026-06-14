package com.tfg.API_TFG.core.service;

import com.tfg.API_TFG.adapter.CodigoDescuentoAdapter;
import com.tfg.API_TFG.core.dto.CodigoDescuentoCreateDTO;
import com.tfg.API_TFG.core.dto.CodigoDescuentoDTO;
import com.tfg.API_TFG.core.entity.CodigoDescuento;
import com.tfg.API_TFG.core.repository.CodigoDescuentoRepository;
import com.tfg.API_TFG.core.service.interfaces.CodigoDescuentoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CodigoDescuentoServiceImpl implements CodigoDescuentoService {

    private final CodigoDescuentoRepository codigoDescuentoRepository;

    @Autowired
    public CodigoDescuentoServiceImpl(
            CodigoDescuentoRepository codigoDescuentoRepository
    ) {
        this.codigoDescuentoRepository = codigoDescuentoRepository;
    }

    @Override
    public List<CodigoDescuentoDTO> getAll() {
        return codigoDescuentoRepository.findAll().stream()
                .map(CodigoDescuentoAdapter::toDTO)
                .toList();
    }

    @Override
    public CodigoDescuentoDTO getById(Integer id) {

        CodigoDescuento codigo = codigoDescuentoRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "No existe el código de descuento con ID: " + id
                        ));

        return CodigoDescuentoAdapter.toDTO(codigo);
    }

    @Override
    @Transactional
    public CodigoDescuentoDTO createCodigoDescuento(
            CodigoDescuentoCreateDTO dto
    ) {

        if (codigoDescuentoRepository.existsByCodigo(dto.codigo())) {
            throw new IllegalArgumentException(
                    "Ya existe un código de descuento con código " + dto.codigo()
            );
        }

        CodigoDescuento codigo = new CodigoDescuento();

        codigo.setCodigo(dto.codigo());
        codigo.setCondicion(dto.condicion());
        codigo.setPorcentajeDescuento(dto.porcentajeDescuento());
        codigo.setActivo(dto.activo());

        codigo = codigoDescuentoRepository.save(codigo);

        return CodigoDescuentoAdapter.toDTO(codigo);
    }

    @Override
    @Transactional
    public CodigoDescuentoDTO updateCodigoDescuento(
            Integer id,
            CodigoDescuentoCreateDTO dto
    ) {

        CodigoDescuento codigo = codigoDescuentoRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "No existe el código de descuento con ID: " + id
                        ));

        if (!codigo.getCodigo().equals(dto.codigo())
                && codigoDescuentoRepository.existsByCodigo(dto.codigo())) {

            throw new IllegalArgumentException(
                    "Ya existe un código de descuento con código " + dto.codigo()
            );
        }

        codigo.setCodigo(dto.codigo());
        codigo.setCondicion(dto.condicion());
        codigo.setPorcentajeDescuento(dto.porcentajeDescuento());
        codigo.setActivo(dto.activo());

        codigo = codigoDescuentoRepository.save(codigo);

        return CodigoDescuentoAdapter.toDTO(codigo);
    }

    @Override
    public CodigoDescuentoDTO getByCodigo(String codigo) {

        CodigoDescuento codigoDescuento =
                codigoDescuentoRepository.findByCodigo(codigo)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "No existe el código de descuento: " + codigo
                                )
                        );

        return CodigoDescuentoAdapter.toDTO(codigoDescuento);
    }
}
