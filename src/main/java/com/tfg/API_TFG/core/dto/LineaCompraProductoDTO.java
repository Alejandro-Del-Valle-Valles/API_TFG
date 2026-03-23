package com.tfg.API_TFG.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Datos de una línea de compra de un producto")
public class LineaCompraProductoDTO extends LineaCompraDTO{

    @Schema(
            description = "Información del producto que contiene la línea de compra",
            implementation = ProductoDTO.class
    )
    @NotNull(message = "El producto no puede ser nulo.")
    private ProductoDTO producto;

    public LineaCompraProductoDTO(Integer numero, ProductoDTO producto) {
        super(numero);
        this.producto = producto;
    }

    public ProductoDTO getProducto() { return producto; }

    public void setProducto(ProductoDTO producto) { this.producto = producto; }
}
