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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
        Optional<Producto> productoExiste = productoRepository.findByNombreIgnoreCase(productoDTO.nombre());
        if(productoExiste.isPresent()) throw new EntityExistsException("Ya existe un producto con el nombre " + productoDTO.nombre());
        Producto producto = new Producto();
        producto.setNombre(productoDTO.nombre());
        producto.setPrecio(productoDTO.precio());
        producto.setStock(productoDTO.stock());
        return ProductoAdapter.toDTO(productoRepository.save(producto));
    }

    @Override
    @Transactional
    public ProductoDTO updateProducto(String nombre, ProductoDTO productoDTO) {
        Producto producto = productoRepository.findByNombreIgnoreCase(nombre)
                .orElseThrow(() -> new EntityNotFoundException("No existe el producto " + nombre));

        if (!nombre.equalsIgnoreCase(productoDTO.nombre())) {
            productoRepository.findByNombreIgnoreCase(productoDTO.nombre())
                    .ifPresent(p -> { throw new EntityExistsException(
                            "Ya existe un producto con el nombre " + productoDTO.nombre()); });
        }
        producto.setNombre(productoDTO.nombre());
        producto.setPrecio(productoDTO.precio());

        List<Alergeno> alergenos = productoDTO.alergenos().stream()
                .map(a -> alergenoRepository.findByNombreIgnoreCase(a.nombre())
                        .orElseThrow(() -> new EntityNotFoundException("No se encontró el alérgeno " + a.nombre())))
                .collect(Collectors.toCollection(ArrayList::new));
        producto.getAlergenos().clear();
        producto.getAlergenos().addAll(alergenos);

        producto.setStock(productoDTO.stock());
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
