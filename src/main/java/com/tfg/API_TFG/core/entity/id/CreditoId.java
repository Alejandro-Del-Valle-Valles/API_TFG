package com.tfg.API_TFG.core.entity.id;

import com.tfg.API_TFG.core.enums.RolParticipante;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class CreditoId implements Serializable {

    @Column(name = "pelicula_id")
    private UUID peliculaId;

    @Column(name = "participante_id")
    private Integer participanteId;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", length = 25)
    private RolParticipante rol;

    public CreditoId() {}

    public CreditoId(UUID peliculaId, Integer participanteId, RolParticipante rol) {
        this.peliculaId = peliculaId;
        this.participanteId = participanteId;
        this.rol = rol;
    }

    public UUID getPeliculaId() { return peliculaId; }
    public void setPeliculaId(UUID peliculaId) { this.peliculaId = peliculaId; }

    public Integer getParticipanteId() { return participanteId; }
    public void setParticipanteId(Integer participanteId) { this.participanteId = participanteId; }

    public RolParticipante getRol() { return rol; }
    public void setRol(RolParticipante rol) { this.rol = rol; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreditoId that = (CreditoId) o;
        return Objects.equals(peliculaId, that.peliculaId)
                && Objects.equals(participanteId, that.participanteId)
                && rol == that.rol;
    }

    @Override
    public int hashCode() {
        return Objects.hash(peliculaId, participanteId, rol);
    }
}