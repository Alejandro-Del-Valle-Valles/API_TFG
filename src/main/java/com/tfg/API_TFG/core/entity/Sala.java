package com.tfg.API_TFG.core.entity;

import com.tfg.API_TFG.core.entity.id.SalaId;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Objects;

@Entity
public class Sala {

    @EmbeddedId
    private SalaId id;

    @ManyToOne
    @MapsId("cineId")
    @JoinColumn(name = "cine_id", nullable = false)
    @Valid
    private Cine cine;

    @NotNull(message = "El aforo de la sala no puede ser nulo.")
    @Positive(message = "El aforo de la sala no puede ser negativo.")
    private Integer aforo;

    public Sala() {}

    public SalaId getId() { return id; }
    public void setId(SalaId id) { this.id = id; }

    public Cine getCine() { return cine; }
    public void setCine(@Valid Cine cine) {
        this.cine = cine;
    }

    // Acceso directo al número de sala desde el id embebido
    public Integer getNumero() { return id != null ? id.getNumeroSala() : null; }

    public Integer getAforo() { return aforo; }
    public void setAforo(Integer aforo) { this.aforo = aforo; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Sala sala = (Sala) o;
        return Objects.equals(id, sala.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
