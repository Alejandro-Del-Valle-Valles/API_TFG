package com.tfg.API_TFG.core.entity;

import com.tfg.API_TFG.core.entity.id.LineaId;
import jakarta.persistence.*;
import jakarta.validation.Valid;

@Entity
public class LineaCompra {

    @EmbeddedId
    private LineaId id;

    @ManyToOne
    @MapsId("compraId")        // Mapea el campo compraId del embeddable
    @JoinColumn(name = "compra_id", nullable = false)
    private Compra compra;

    //TODO: Implementar las entidades Entrada, Sesión y Proyección. Crear las relaciones entre estas.
}
