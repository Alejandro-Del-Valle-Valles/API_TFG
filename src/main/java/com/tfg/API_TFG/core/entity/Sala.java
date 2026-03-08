package com.tfg.API_TFG.core.entity;

import com.tfg.API_TFG.core.entity.id.SalaId;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.ArrayList;
import java.util.List;
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

    @OneToMany(mappedBy = "sala", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<@Valid Proyeccion> proyecciones = new ArrayList<>();

    public Sala() {}

    public SalaId getId() { return id; }
    public void setId(SalaId id) { this.id = id; }

    public Cine getCine() { return cine; }
    public void setCine(@Valid Cine cine) {
        this.cine = cine;
    }

    // Acceso directo al número de sala desde el id embebido
    public Integer getNumero() { return id != null ? id.getNumero() : null; }

    public void setNumero(Integer numero) {
        if(this.id != null) this.id.setNumero(numero);
    }

    public Integer getAforo() { return aforo; }
    public void setAforo(Integer aforo) { this.aforo = aforo; }

    public List<Proyeccion> getProyecciones() {
        return proyecciones;
    }

    public void setProyecciones(List<@Valid Proyeccion> proyecciones) {
        this.proyecciones = proyecciones;
    }

    /**
     * Añade la proyección si no la contiene ya y establece la relación.
     * @param proyeccion Proyeccion a añadir.
     */
    public void addProyeccion(@Valid Proyeccion proyeccion) {
        if(proyeccion == null) throw new IllegalArgumentException("La proyección no puede ser nula.");
        if(!this.proyecciones.contains(proyeccion)) {
            proyeccion.setSala(this);
            this.proyecciones.add(proyeccion);
        }
    }

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
