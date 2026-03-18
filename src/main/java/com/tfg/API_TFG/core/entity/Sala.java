package com.tfg.API_TFG.core.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Sala {

    @Id
    @Positive(message = "El número de la sala debe ser positivo y mayor que 0.")
    private Integer numSala;

    @NotNull(message = "El aforo de la sala no puede ser nulo.")
    @Positive(message = "El aforo de la sala no puede ser negativo.")
    private Integer aforo;

    @OneToMany(mappedBy = "sala", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<@Valid Sesion> proyecciones = new ArrayList<>();

    public Sala() {}

    public Integer getNumSala() {
        return numSala;
    }

    public void setNumSala(Integer numSala) {
        this.numSala = numSala;
    }

    public Integer getAforo() { return aforo; }
    public void setAforo(Integer aforo) { this.aforo = aforo; }

    public List<Sesion> getProyecciones() {
        return proyecciones;
    }

    public void setProyecciones(List<@Valid Sesion> proyecciones) {
        this.proyecciones = proyecciones;
    }

    /**
     * Añade la proyección si no la contiene ya y establece la relación.
     * @param sesion Proyeccion a añadir.
     */
    public void addProyeccion(@Valid Sesion sesion) {
        if(sesion == null) throw new IllegalArgumentException("La proyección no puede ser nula.");
        if(!this.proyecciones.contains(sesion)) {
            sesion.setSala(this);
            this.proyecciones.add(sesion);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Sala sala = (Sala) o;
        return Objects.equals(numSala, sala.numSala);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numSala);
    }
}
