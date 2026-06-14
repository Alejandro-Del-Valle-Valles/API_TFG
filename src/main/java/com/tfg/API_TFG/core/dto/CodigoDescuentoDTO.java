package com.tfg.API_TFG.core.dto;

import com.tfg.API_TFG.core.enums.CondicionDescuento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.Objects;

@Schema(description = "Información de un código de descuento.")
public class CodigoDescuentoDTO {

    @Schema(
            description = "ID del código de descuento",
            example = "1"
    )
    @NotNull(message = "El ID no puede ser nulo.")
    private Integer id;

    @Schema(
            description = "Código que el usuario introducirá para obtener el descuento",
            example = "VERANO2025"
    )
    @NotNull(message = "El código no puede ser nulo.")
    @NotBlank(message = "El código no puede estar vacío.")
    @Length(max = 25, message = "El código no puede contener más de 25 caracteres.")
    private String codigo;

    @Schema(
            description = "Condición de aplicación del descuento",
            example = "PELICULA",
            allowableValues = {"COMIDA", "PELICULA", "TODOS"},
            implementation = CondicionDescuento.class
    )
    @NotNull(message = "Debe especificarse una condición.")
    private CondicionDescuento condicion;

    @Schema(
            description = "Porcentaje de descuento aplicado",
            example = "15"
    )
    @NotNull(message = "El porcentaje no puede ser nulo.")
    @Min(value = 1, message = "El porcentaje mínimo es 1.")
    @Max(value = 100, message = "El porcentaje máximo es 100.")
    private Integer porcentajeDescuento;

    @Schema(
            description = "Indica si el código está activo",
            example = "true"
    )
    @NotNull(message = "Debe indicarse si el código está activo.")
    private Boolean activo;

    public CodigoDescuentoDTO(Integer id,
                              String codigo,
                              CondicionDescuento condicion,
                              Integer porcentajeDescuento,
                              Boolean activo) {
        this.id = id;
        this.codigo = codigo;
        this.condicion = condicion;
        this.porcentajeDescuento = porcentajeDescuento;
        this.activo = activo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public CondicionDescuento getCondicion() {
        return condicion;
    }

    public void setCondicion(CondicionDescuento condicion) {
        this.condicion = condicion;
    }

    public Integer getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(Integer porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CodigoDescuentoDTO that = (CodigoDescuentoDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}