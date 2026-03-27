package com.tfg.API_TFG.core.entity;

import com.tfg.API_TFG.core.entity.id.LineaId;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;

import java.util.List;
import java.util.Objects;

@Entity
public class LineaCompra {

    @EmbeddedId
    private LineaId id;

    @ManyToOne
    @MapsId("compraId")
    @JoinColumn(name = "compra_id", nullable = false)
    private @Valid Compra compra;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = true)
    private @Valid Producto producto;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "num_sala", referencedColumnName = "num_sala", nullable = true),
            @JoinColumn(name = "pelicula_id", referencedColumnName = "pelicula_id", nullable = true),
            @JoinColumn(name = "horario_sesion", referencedColumnName = "horario_sesion", nullable = true),
            @JoinColumn(name = "fila", referencedColumnName = "fila", nullable = true),
            @JoinColumn(name = "butaca", referencedColumnName = "butaca", nullable = true)
    })
    private @Valid Entrada entrada;

    @AssertTrue(message = "La línea de compra debe tener exactamente una Entrada o un Producto, no ambos ni ninguno.")
    private boolean isXorValido() { return (entrada != null) ^ (producto != null); }

    public LineaCompra() { }

    public LineaId getId() { return id; }

    public void setId(LineaId id) { this.id = id; }

    public Compra getCompra() { return compra; }

    public void setCompra(Compra compra) { this.compra = compra; }

    public Producto getProducto() { return producto; }

    public void setProducto(@Valid Producto producto) { this.producto = producto; }

    public Entrada getEntrada() { return entrada; }

    public void setEntrada(@Valid Entrada entrada) { this.entrada = entrada; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LineaCompra that = (LineaCompra) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hashCode(id); }
}
