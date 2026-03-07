package com.tfg.API_TFG.core.entity.id;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Clase que serializa la PK de LineaCompra porque es una entidad débil.
 */
public class LineaId implements Serializable {

    private UUID compra;

    private Integer numLinea;

    public LineaId() { }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LineaId lineaId = (LineaId) o;
        return Objects.equals(compra, lineaId.compra) && Objects.equals(numLinea, lineaId.numLinea);
    }

    @Override
    public int hashCode() {
        return Objects.hash(compra, numLinea);
    }
}
