package com.tfg.API_TFG.core.service;

import com.tfg.API_TFG.adapter.ProductoAdapter;
import com.tfg.API_TFG.core.dto.ProductoDTO;
import com.tfg.API_TFG.core.entity.Alergeno;
import com.tfg.API_TFG.core.entity.Producto;
import com.tfg.API_TFG.core.repository.AlergenoRepository;
import com.tfg.API_TFG.core.service.interfaces.ProductoService;
import com.tfg.API_TFG.core.repository.ProductoRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceImpl implements ProductoService {


    private final ProductoRepository productoRepository;
    private final AlergenoRepository alergenoRepository;

    @Autowired
    public ProductoServiceImpl(ProductoRepository productoRepository, AlergenoRepository alergenoRepository) {
        this.productoRepository = productoRepository;
        this.alergenoRepository = alergenoRepository;
    }

    @Override
    public List<ProductoDTO> getAll() {
        return productoRepository.findAll().stream()
                .map(ProductoAdapter::toDTO)
                .toList();
    }

    @Override
    public ProductoDTO getByNombre(String nombre) {
        return ProductoAdapter.toDTO(productoRepository.findByNombreIgnoreCase(nombre)
                .orElseThrow(() -> new EntityNotFoundException("No existe un producto con el nombre " + nombre)));
    }

    @Override
    public ProductoDTO createProducto(ProductoDTO productoDTO) {
        Optional<Producto> producto = productoRepository.findByNombreIgnoreCase(productoDTO.getNombre());
        if(!producto.isEmpty()) throw new EntityExistsException("Ya existe un producto con el nombre " + productoDTO.getNombre());
        productoRepository.save(producto.get());
        return ProductoAdapter.toDTO(producto.get());
    }

    @Override
    @Transactional
    public ProductoDTO updateProducto(String nombre, ProductoDTO productoDTO) {
        Producto producto = productoRepository.findByNombreIgnoreCase(nombre)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No existe un producto con el nombre " + nombre
                ));
        producto.setNombre(productoDTO.getNombre());
        producto.setPrecio(productoDTO.getPrecio());
        List<Alergeno> alergenos = productoDTO.getAlergenos().stream()
                .map(alergenoDTO -> alergenoRepository.findByNombreIgnoreCase(alergenoDTO.getNombre())
                        .orElseThrow(() -> new EntityNotFoundException(
                                "No se encontró el alérgeno " + alergenoDTO.getNombre()
                        ))
                )
                .toList();
        producto.setAlergenos(alergenos);
        return ProductoAdapter.toDTO(productoRepository.save(producto));
    }

    @Override
    public ProductoDTO deleteProducto(String nombre) {
        Producto producto = productoRepository.findByNombreIgnoreCase(nombre)
                .orElseThrow(() -> new EntityNotFoundException("No existe un producto con el nombre " +  nombre));
        productoRepository.delete(producto);
        return ProductoAdapter.toDTO(producto);
    }
}
