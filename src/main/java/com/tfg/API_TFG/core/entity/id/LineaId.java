package com.tfg.API_TFG.core.entity.id;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Clase que serializa la PK de LineaCompra porque es una entidad débil.
 */
@Embeddable
public class LineaId implements Serializable {

    @Column(name = "compra_id")
    private UUID compraId;

    @Column(name = "num_linea")
    private Integer numLinea;

    public LineaId() {}
    public LineaId(UUID compraId, Integer numLinea) {
        this.compraId = compraId;
        this.numLinea = numLinea;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LineaId that = (LineaId) o;
        return Objects.equals(compraId, that.compraId) &&
               Objects.equals(numLinea, that.numLinea);
    }

    @Override
    public int hashCode() {
        return Objects.hash(compraId, numLinea);
    }
}
