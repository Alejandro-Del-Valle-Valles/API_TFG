package com.tfg.API_TFG.core.entity;

import com.tfg.API_TFG.core.enums.RolParticipante;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;
import java.util.UUID;

@Entity
public class Credito {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "pelicula_id", nullable = false)
    private Pelicula pelicula;

    @ManyToOne
    @JoinColumn(name = "participante_id")
    private @Valid Participante participante;

    @NotNull(message = "El rol del participante no puede ser nulo.")
    @Enumerated(EnumType.STRING)
    @Column(length = 25)
    private RolParticipante rol;

    public Credito() { }

    public UUID getId() { return id; }

    public void setId(UUID id) { this.id = id; }

    public Pelicula getPelicula() { return pelicula; }

    public void setPelicula(@Valid Pelicula pelicula) { this.pelicula = pelicula; }

    public Participante getParticipante() { return participante; }

    public void setParticipante(@Valid Participante participante) { this.participante = participante; }

    public RolParticipante getRol() { return rol; }

    public void setRol(RolParticipante role) { this.rol = role; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Credito credito = (Credito) o;
        return Objects.equals(id, credito.id);
    }

    @Override
    public int hashCode() { return Objects.hashCode(id); }
}
