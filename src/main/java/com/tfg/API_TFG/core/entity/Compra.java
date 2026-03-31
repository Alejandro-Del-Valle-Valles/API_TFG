package com.tfg.API_TFG.core.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private @Valid Usuario usuario;

    @NotEmpty(message = "La compra debe contener al menos una línea.")
    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<@Valid LineaCompra> lineaCompras = new ArrayList<>();

    @AssertTrue(message = "La compra debe tener al menos una línea con entrada, y no puede haber 2 líneas de compra con el mismo número.")
    private boolean hasLineaConEntrada() {
        return lineaCompras != null && lineaCompras.stream().anyMatch(lc -> lc.getEntrada() != null);
    }

    public Compra() { }

    public UUID getId() { return id; }

    public void setId(UUID id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }

    public void setUsuario(@Valid Usuario usuario) { this.usuario = usuario; }

    public List<LineaCompra> getLineaCompras() { return lineaCompras; }

    public void setLineaCompras(List<@Valid LineaCompra> lineaCompras) { this.lineaCompras = lineaCompras; }

    /**
     * Añade la linea de compra si no la contiene ya y establece la relación entre ambos.
     * @param lineaCompra LineaCompra a añadir.
     */
    public void addLineaCompra(@Valid LineaCompra lineaCompra) {
        if(lineaCompra == null) throw new IllegalArgumentException("La línea de compra no puede ser nula.");
        if(!this.lineaCompras.contains(lineaCompra)) {
            lineaCompra.setCompra(this);
            this.lineaCompras.add(lineaCompra);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Compra compra = (Compra) o;
        return Objects.equals(id, compra.id);
    }

    @Override
    public int hashCode() { return Objects.hashCode(id); }
}
