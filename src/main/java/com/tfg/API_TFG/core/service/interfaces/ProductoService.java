package com.tfg.API_TFG.core.service.interfaces;

import com.tfg.API_TFG.core.dto.ProductoDTO;

import java.util.List;

public interface ProductoService {

    List<ProductoDTO> getAll();
    ProductoDTO getByNombre(String nombre);
    ProductoDTO createProducto(ProductoDTO productoDTO);
    ProductoDTO updateProducto(String nombre, ProductoDTO productoDTO);
    ProductoDTO deleteProducto(String nombre);
}
