package com.tfg.API_TFG.core.entity;

import com.tfg.API_TFG.core.entity.id.EntradaId;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Entrada {

    @EmbeddedId
    private EntradaId id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "cine_id", referencedColumnName = "cine_id", insertable = false, updatable = false),
            @JoinColumn(name = "num_sala", referencedColumnName = "num_sala", insertable = false, updatable = false),
            @JoinColumn(name = "pelicula_id", referencedColumnName = "pelicula_id", insertable = false, updatable = false),
            @JoinColumn(name = "horario_sesion", referencedColumnName = "horario_sesion", insertable = false, updatable = false)
    })
    private @Valid Proyeccion proyeccion;

    @NotNull(message = "El precio no puede ser nulo.")
    @PositiveOrZero(message = "El precio debe ser 0 o superior.")
    @Column(precision = 4, scale = 2)
    private BigDecimal precio;

    @OneToOne(mappedBy = "entrada")
    private @Valid LineaCompra lineaCompra;

    public Entrada() { }

    public EntradaId getId() {
        return id;
    }

    public void setId(EntradaId id) {
        this.id = id;
    }

    public Proyeccion getProyeccion() {
        return proyeccion;
    }

    public void setProyeccion(@Valid Proyeccion proyeccion) {
        this.proyeccion = proyeccion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public LineaCompra getLineaCompra() {
        return lineaCompra;
    }

    public void setLineaCompra(@Valid LineaCompra lineaCompra) {
        this.lineaCompra = lineaCompra;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Entrada entrada = (Entrada) o;
        return Objects.equals(id, entrada.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
