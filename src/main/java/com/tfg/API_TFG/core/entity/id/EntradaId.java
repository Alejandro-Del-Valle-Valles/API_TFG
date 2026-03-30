package com.tfg.API_TFG.core.entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Clase que serializa la PK de entrada al ser esta un entidad debil.
 */
@Embeddable
public class EntradaId implements Serializable {

    @Embedded
    private SesionId sesionId;

    @Column(name = "fila")
    private Integer fila;

    @Column(name = "butaca")
    private Integer butaca;

    public EntradaId() { }

    public EntradaId(SesionId sesionId, Integer fila, Integer butaca) {
        this.sesionId = sesionId;
        this.fila = fila;
        this.butaca = butaca;
    }

    public SesionId getSesionId() { return sesionId; }

    public void setSesionId(SesionId sesionId) { this.sesionId = sesionId; }

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
        return Objects.equals(sesionId, entradaId.sesionId) && Objects.equals(fila, entradaId.fila) && Objects.equals(butaca, entradaId.butaca);
    }

    @Override
    public int hashCode() { return Objects.hash(sesionId, fila, butaca); }
}
