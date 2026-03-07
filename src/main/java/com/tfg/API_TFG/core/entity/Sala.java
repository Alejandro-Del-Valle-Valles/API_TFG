package com.tfg.API_TFG.core.entity;

import com.tfg.API_TFG.core.entity.id.SalaId;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Objects;

@Entity
@IdClass(SalaId.class)
public class Sala {

    @Id
    @ManyToOne
    @JoinColumn(name = "cine_id", nullable = false)
    private @Valid Cine cine;

    @Id
    @NotNull(message = "El numero de la sala no puede ser nulo")
    @Positive(message = "El numero de la sala no puede ser negativo ni menor a 1.")
    private Integer numero;

    @NotNull(message = "El aforo de la sala no puede ser nulo.")
    @Positive(message = "El aforo de la sala no puede ser negativo.")
    private Integer aforo;

    public Sala() { }

    public Cine getCine() {
        return cine;
    }

    public void setCine(@Valid Cine cine) {
        this.cine = cine;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Integer getAforo() {
        return aforo;
    }

    public void setAforo(Integer aforo) {
        this.aforo = aforo;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Sala sala = (Sala) o;
        return Objects.equals(cine, sala.cine) && Objects.equals(numero, sala.numero);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cine, numero);
    }
}
