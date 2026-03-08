package com.tfg.API_TFG.core.entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Clase que serializa la PK de Proyeccion al ser esta una entidad asociativa
 */
@Embeddable
public class ProyeccionId {

    @Column(name = "cine_id")
    private Integer cineId;

    @Column(name = "num_sala")
    private Integer numSala;

    @Column(name = "pelicula_id")
    private UUID peliculaId;

    @Column(name = "horario_sesion")
    private LocalDateTime horarioSesion;

    public ProyeccionId() { }

    public ProyeccionId(Integer cineId, Integer numSala, UUID peliculaId, LocalDateTime horarioSesion) {
        this.cineId = cineId;
        this.numSala = numSala;
        this.peliculaId = peliculaId;
        this.horarioSesion = horarioSesion;
    }

    public Integer getCineId() {
        return cineId;
    }

    public void setCineId(Integer cineId) {
        this.cineId = cineId;
    }

    public Integer getNumSala() {
        return numSala;
    }

    public void setNumSala(Integer numSala) {
        this.numSala = numSala;
    }

    public UUID getPeliculaId() {
        return peliculaId;
    }

    public void setPeliculaId(UUID peliculaId) {
        this.peliculaId = peliculaId;
    }

    public LocalDateTime getHorarioSesion() {
        return horarioSesion;
    }

    public void setHorarioSesion(LocalDateTime horarioSesion) {
        this.horarioSesion = horarioSesion;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProyeccionId that = (ProyeccionId) o;
        return Objects.equals(cineId, that.cineId) && Objects.equals(numSala, that.numSala) &&
                Objects.equals(peliculaId, that.peliculaId) && Objects.equals(horarioSesion, that.horarioSesion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cineId, numSala, peliculaId, horarioSesion);
    }
}
