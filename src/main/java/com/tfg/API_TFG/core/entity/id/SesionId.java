package com.tfg.API_TFG.core.entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Clase que serializa la PK de Sesion, ya que es entidad debil.
 */
@Embeddable
public class SesionId implements Serializable {

    @Column(name = "pelicula_id")
    private UUID peliculaId;

    @Column(name = "horario")
    private LocalDateTime horario;

    public SesionId() { }

    public SesionId(UUID peliculaId, LocalDateTime horario) {
        this.peliculaId = peliculaId;
        this.horario = horario;
    }

    public UUID getPeliculaId() {
        return peliculaId;
    }

    public void setPeliculaId(UUID peliculaId) {
        this.peliculaId = peliculaId;
    }

    public LocalDateTime getHorario() {
        return horario;
    }

    public void setHorario(LocalDateTime horario) {
        this.horario = horario;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SesionId sesionId = (SesionId) o;
        return Objects.equals(peliculaId, sesionId.peliculaId) && Objects.equals(horario, sesionId.horario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(peliculaId, horario);
    }
}
