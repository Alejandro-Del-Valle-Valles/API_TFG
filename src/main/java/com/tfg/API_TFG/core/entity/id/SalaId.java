package com.tfg.API_TFG.core.entity.id;

import java.io.Serializable;
import java.util.Objects;

/**
 * Clase que serializa la clave de Sala al ser esta una entidad debil
 */
public class SalaId implements Serializable {

    private Long cine;
    private Integer numeroSala;

    public SalaId() { }

    public SalaId(Long cine, Integer numeroSala) {
        this.cine = cine;
        this.numeroSala = numeroSala;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SalaId salaId = (SalaId) o;
        return Objects.equals(cine, salaId.cine) && Objects.equals(numeroSala, salaId.numeroSala);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cine, numeroSala);
    }
}
