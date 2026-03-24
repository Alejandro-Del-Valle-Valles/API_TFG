package com.tfg.API_TFG.core.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

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

    @NotNull(message = "La fecha de compra no puede ser nula.")
    @NotBlank(message = "La fecha de compra no puede estar en blanco.")
    @PastOrPresent(message = "La fecha debe ser pasada o presente, no futura.")
    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private @Valid Usuario usuario;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true)
    private @Valid List<@Valid LineaCompra> lineaCompras = new ArrayList<>();

    public Compra() { }

    public UUID getId() { return id; }

    public void setId(UUID id) { this.id = id; }

    public LocalDateTime getFecha() { return fecha; }

    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

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
