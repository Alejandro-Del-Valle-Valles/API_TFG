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

    @Column(name = "num_butaca")
    private Integer numButaca;

    public EntradaId() { }

    public EntradaId(Integer numSala, UUID peliculaId, LocalDateTime horarioSesion, Integer numButaca) {
        this.numSala = numSala;
        this.peliculaId = peliculaId;
        this.horarioSesion = horarioSesion;
        this.numButaca = numButaca;
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

    public Integer getNumButaca() {
        return numButaca;
    }

    public void setNumButaca(Integer numButaca) {
        this.numButaca = numButaca;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EntradaId entradaId = (EntradaId) o;
        return Objects.equals(numSala, entradaId.numSala) &&
                Objects.equals(peliculaId, entradaId.peliculaId) && Objects.equals(horarioSesion, entradaId.horarioSesion)
                && Objects.equals(numButaca, entradaId.numButaca);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numSala, peliculaId, horarioSesion, numButaca);
    }
}
