package com.tfg.API_TFG.core.entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

/**
 * Clase que serializa la clave de Sala al ser esta una entidad debil
 */
@Embeddable
public class SalaId implements Serializable {

    @Column(name = "cine_id")
    private Integer cineId;

    @Column(name = "numero")
    private Integer numero;

    public SalaId() {}

    public SalaId(Integer cineId, Integer numero) {
        this.cineId = cineId;
        this.numero = numero;
    }

    public Integer getCineId() { return cineId; }
    public void setCineId(Integer cineId) { this.cineId = cineId; }

    public Integer getNumero() { return numero; }
    public void setNumero(Integer numeroSala) { this.numero = numeroSala; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SalaId salaId = (SalaId) o;
        return Objects.equals(cineId, salaId.cineId) &&
               Objects.equals(numero, salaId.numero);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cineId, numero);
    }
}
