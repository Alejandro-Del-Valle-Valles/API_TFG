package com.tfg.API_TFG.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Información para crear una línea de compra con un producto.")
public class LineaCompraProductoCreateDTO extends LineaCompraDTO{

    @Schema(
            description = "ID del producto a añadir",
            example = "2"
    )
    @NotNull(message = "El ID del producto no puede ser nulo.")
    @Positive(message = "El ID del producto no puede ser inferior a 1.")
    private Integer productoId;

    public LineaCompraProductoCreateDTO(Integer numero, Integer productoId) {
        super(numero);
        this.productoId = productoId;
    }

    public Integer getProductoId() {
        return productoId;
    }

    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
    }
}
