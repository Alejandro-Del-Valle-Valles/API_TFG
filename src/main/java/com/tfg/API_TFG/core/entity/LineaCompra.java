package com.tfg.API_TFG.core.entity;

import com.tfg.API_TFG.core.entity.id.LineaId;
import jakarta.persistence.*;
import jakarta.validation.Valid;

@Entity
@IdClass(LineaId.class)
public class LineaCompra {

    @Id
    @ManyToOne
    @JoinColumn(name = "compra_id", nullable = false)
    private @Valid Compra compra;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer numLinea;

    //TODO: Implementar las entidades Entrada, Sesión y Proyección. Crear las relaciones entre estas.
}
