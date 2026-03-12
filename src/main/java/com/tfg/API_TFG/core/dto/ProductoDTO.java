package com.tfg.API_TFG.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Schema(description = "Información de un producto.")
public class ProductoDTO {

    @Schema(
            description = "Nombre del producto. Es único por cada producto.",
            example = "Palomitas"
    )
    @NotNull(message = "El nombre del producto no puede ser nulo.")
    @NotBlank(message = "El nombre del producto no puede estar vacío.")
    @Length(max = 50, message = "El nombre del producto no puede contener más de 50 caracteres.")
    private String nombre;

    @Schema(
            description = "Precio del producto en euros. Máximo 5 dígitios de los cuales, 2 son decimales.",
            example = "3.50"
    )
    @NotNull(message = "El precio del producto no puede ser nulo.")
    @PositiveOrZero(message = "El precio del producto no puede ser negativo.")
    private BigDecimal precio;

    @Schema(
            description = "Lista de los alérgenos del producto si contiene.",
            example = "[{\"nombre\": \"Gluten\"}]"
    )
    private List<@Valid AlergenoDTO> alergenos;

    public ProductoDTO(String nombre, BigDecimal precio, List<@Valid AlergenoDTO> alergenos) {
        this.nombre = nombre;
        this.precio = precio;
        this.alergenos = alergenos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public List<AlergenoDTO> getAlergenos() {
        return alergenos;
    }

    public void setAlergenos(List<@Valid AlergenoDTO> alergenos) {
        this.alergenos = alergenos;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProductoDTO that = (ProductoDTO) o;
        return Objects.equals(nombre, that.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nombre);
    }
}
