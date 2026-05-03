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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("sesionId")
    @JoinColumns({
            @JoinColumn(name = "num_sala", referencedColumnName = "num_sala"),
            @JoinColumn(name = "pelicula_id", referencedColumnName = "pelicula_id"),
            @JoinColumn(name = "horario_sesion", referencedColumnName = "horario_sesion")
    })
    private @Valid Sesion sesion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tipo", referencedColumnName = "id", nullable = false)
    @NotNull(message = "El tipo de entrada no puede ser nulo.")
    private @Valid TipoEntrada tipo;

    @OneToOne(mappedBy = "entrada", cascade = CascadeType.ALL, orphanRemoval = true)
    private @Valid LineaCompra lineaCompra;

    public Entrada() { }

    public EntradaId getId() { return id; }

    public void setId(EntradaId id) { this.id = id; }

    public Sesion getSesion() { return sesion; }

    public void setSesion(@Valid Sesion sesion) { this.sesion = sesion; }

    public TipoEntrada getTipo() { return tipo; }

    public void setTipo(TipoEntrada tipo) { this.tipo = tipo; }

    public LineaCompra getLineaCompra() { return lineaCompra; }

    public void setLineaCompra(@Valid LineaCompra lineaCompra) { this.lineaCompra = lineaCompra; }

    @PreRemove
    private void preRemove() {
        if(lineaCompra != null) lineaCompra.setEntrada(null);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Entrada entrada = (Entrada) o;
        return Objects.equals(id, entrada.id);
    }

    @Override
    public int hashCode() { return Objects.hashCode(id); }
}
