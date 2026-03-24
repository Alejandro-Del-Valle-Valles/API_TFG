package com.tfg.API_TFG.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;

@Schema(description = "Información para crear una línea de compra con un producto.")
public class LineaCompraProductoCreateDTO extends LineaCompraDTO{

    @Schema(
            description = "Nombre del producto a añadir",
            example = "Palomitas"
    )
    @NotNull(message = "El nombre del producto no puede ser nulo.")
    @NotBlank(message = "El nombre del producto no puede estar vacío.")
    @Length(max = 50, message = "El nombre del producto no puede tener más de 50 caracteres.")
    private String nombreProducto;

    public LineaCompraProductoCreateDTO(Integer numero, String nombreProducto) {
        super(numero);
        this.nombreProducto = nombreProducto;
    }

    public String getNombreProducto() { return nombreProducto; }

    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
}
