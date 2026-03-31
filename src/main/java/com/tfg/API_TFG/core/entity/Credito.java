package com.tfg.API_TFG.core.entity;

import com.tfg.API_TFG.core.entity.id.CreditoId;
import com.tfg.API_TFG.core.enums.RolParticipante;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

@Entity
@Table(name = "credito")
public class Credito {

    @EmbeddedId
    private CreditoId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("peliculaId")
    @JoinColumn(name = "pelicula_id", nullable = false)
    private Pelicula pelicula;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("participanteId")
    @JoinColumn(name = "participante_id", nullable = false)
    private Participante participante;

    @NotNull(message = "El rol del participante no puede ser nulo.")
    @Enumerated(EnumType.STRING)
    @Column(name = "rol", length = 25, nullable = false, insertable = false, updatable = false)
    private RolParticipante rol;

    public Credito() {}

    public CreditoId getId() { return id; }
    public void setId(CreditoId id) { this.id = id; }

    public Pelicula getPelicula() { return pelicula; }
    public void setPelicula(@Valid Pelicula pelicula) { this.pelicula = pelicula; }

    public Participante getParticipante() { return participante; }
    public void setParticipante(@Valid Participante participante) { this.participante = participante; }

    public RolParticipante getRol() { return rol; }
    public void setRol(RolParticipante rol) {
        this.rol = rol;
        if (this.id == null) this.id = new CreditoId();
        this.id.setRol(rol);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Credito credito = (Credito) o;
        return Objects.equals(id, credito.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}