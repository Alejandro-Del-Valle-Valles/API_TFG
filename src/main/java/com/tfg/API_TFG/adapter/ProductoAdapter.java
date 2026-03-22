package com.tfg.API_TFG.adapter;

import com.tfg.API_TFG.core.dto.ProductoDTO;
import com.tfg.API_TFG.core.entity.Producto;

import java.util.stream.Collectors;

public class ProductoAdapter {

    /**
     * Crea y devuelve un ProductoDTO partiendo de los datos de un Producto.
     * @param producto Producto del que obtener los datos
     * @return ProductoDTO
     */
    public static ProductoDTO toDTO(Producto producto) {
        return new ProductoDTO(producto.getNombre(), producto.getPrecio(), producto.getStock(),
                producto.getAlergenos().stream()
                .map(AlergenoAdapter::toDTO)
                .toList());
    }
}
