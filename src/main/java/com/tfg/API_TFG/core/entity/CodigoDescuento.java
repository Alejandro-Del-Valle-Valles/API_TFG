package com.tfg.API_TFG.core.entity;

import com.tfg.API_TFG.core.enums.CondicionDescuento;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class CodigoDescuento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "El código de descuento no puede ser nulo.")
    @NotBlank(message = "El código de descuento no puede estar vacío.")
    @Column(length = 25,
            unique = true,
            nullable = false)
    private String codigo;

    @NotNull(message = "El código de descuento debe contener una condición para saber a qué se aplicará")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CondicionDescuento condicion;

    @NotNull(message = "El porcentaje de descuento no puede ser nulo.")
    @Positive(message = "El porcentaje de descuento no puede ser negativo.")
    @Min(1)
    @Max(100)
    @Column(nullable = false)
    private Integer porcentajeDescuento;

    @Column(nullable = false)
    private boolean activo;

    public CodigoDescuento(){ }


    public Integer getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public CondicionDescuento getCondicion() {
        return condicion;
    }

    public Integer getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setCondicion(CondicionDescuento condicion) {
        this.condicion = condicion;
    }

    public void setPorcentajeDescuento(Integer porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
