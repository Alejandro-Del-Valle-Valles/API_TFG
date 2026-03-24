package com.tfg.API_TFG.core.entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Clase que serializa la PK de entrada al ser esta un entidad debil.
 */
@Embeddable
public class EntradaId implements Serializable {

    @Column(name = "num_sala")
    private Integer numSala;

    @Column(name = "pelicula_id")
    private UUID peliculaId;

    @Column(name = "horario_sesion")
    private LocalDateTime horarioSesion;

    @Column(name = "fila")
    private Integer fila;

    @Column(name = "butaca")
    private Integer butaca;

    public EntradaId() { }

    public EntradaId(Integer numSala, UUID peliculaId, LocalDateTime horarioSesion, Integer fila, Integer butaca) {
        this.numSala = numSala;
        this.peliculaId = peliculaId;
        this.horarioSesion = horarioSesion;
        this.fila = fila;
        this.butaca = butaca;
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

    public Integer getFila() { return fila; }

    public void setFila(Integer numFila) { this.fila = numFila; }

    public Integer getButaca() {
        return butaca;
    }

    public void setButaca(Integer butaca) {
        this.butaca = butaca;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EntradaId entradaId = (EntradaId) o;
        return Objects.equals(numSala, entradaId.numSala) && Objects.equals(peliculaId, entradaId.peliculaId) &&
                Objects.equals(horarioSesion, entradaId.horarioSesion) && Objects.equals(fila, entradaId.fila) &&
                Objects.equals(butaca, entradaId.butaca);
    }

    @Override
    public int hashCode() { return Objects.hash(numSala, peliculaId, horarioSesion, fila, butaca); }
}
