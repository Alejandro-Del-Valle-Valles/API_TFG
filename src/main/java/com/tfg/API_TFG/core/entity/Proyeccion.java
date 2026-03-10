package com.tfg.API_TFG.core.entity;

import com.tfg.API_TFG.core.entity.id.ProyeccionId;
import jakarta.persistence.*;
import jakarta.validation.Valid;

import java.util.Objects;

@Entity
public class Proyeccion {

    @EmbeddedId
    private ProyeccionId id;

    @ManyToOne
    @JoinColumn(name = "num_sala", referencedColumnName = "numSala", insertable = false, updatable = false)
    private @Valid Sala sala;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "pelicula_id", referencedColumnName = "pelicula_id", insertable = false, updatable = false),
            @JoinColumn(name = "horario_sesion", referencedColumnName = "horario", insertable = false, updatable = false)
    })
    private @Valid Sesion sesion;



    public Proyeccion() { }

    public ProyeccionId getId() {
        return id;
    }

    public void setId(ProyeccionId id) {
        this.id = id;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(@Valid Sala sala) {
        this.sala = sala;
    }

    public Sesion getSesion() {
        return sesion;
    }

    public void setSesion(@Valid Sesion sesion) {
        this.sesion = sesion;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Proyeccion that = (Proyeccion) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
