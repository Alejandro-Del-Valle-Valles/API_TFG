package com.tfg.API_TFG.core.entity.id;

import java.io.Serializable;
import java.util.Objects;

/**
 * Clase que serializa la clave de Sala al ser esta una entidad debil
 */
@Embeddable
public class SalaId implements Serializable {

    @Column(name = "cine_id")
    private Long cineId;

    @Column(name = "numero_sala")
    private Integer numeroSala;

    public SalaId() {}

    public SalaId(Long cineId, Integer numeroSala) {
        this.cineId = cineId;
        this.numeroSala = numeroSala;
    }

    public Long getCineId() { return cineId; }
    public void setCineId(Long cineId) { this.cineId = cineId; }

    public Integer getNumeroSala() { return numeroSala; }
    public void setNumeroSala(Integer numeroSala) { this.numeroSala = numeroSala; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SalaId salaId = (SalaId) o;
        return Objects.equals(cineId, salaId.cineId) &&
               Objects.equals(numeroSala, salaId.numeroSala);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cineId, numeroSala);
    }
}
