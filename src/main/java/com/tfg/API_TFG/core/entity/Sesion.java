package com.tfg.API_TFG.core.entity;

import com.tfg.API_TFG.core.entity.id.SesionId;
import jakarta.persistence.*;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class Sesion {

    @EmbeddedId
    private SesionId id;

    @ManyToOne
    @MapsId("peliculaId")
    @JoinColumn(name = "pelicula_id", nullable = false)
    private @Valid Pelicula pelicula;

    @OneToMany(mappedBy = "sesion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<@Valid Proyeccion> proyecciones;

    public Sesion() { }

    public SesionId getId() {
        return id;
    }

    public void setId(SesionId id) {
        this.id = id;
    }

    public Pelicula getPelicula() {
        return pelicula;
    }

    public void setPelicula(@Valid Pelicula pelicula) {
        this.pelicula = pelicula;
    }

    public LocalDateTime getHorario() {
        return this.id != null ? this.id.getHorario() : null;
    }

    public void setHorario(LocalDateTime horario) {
        if(this.id != null) this.id.setHorario(horario);
    }

    public List<Proyeccion> getProyecciones() {
        return proyecciones;
    }

    public void setProyecciones(List<@Valid Proyeccion> proyecciones) {
        this.proyecciones = proyecciones;
    }

    /**
     * Añade una proyección si no la tiene ya y establece la relación entre ambos.
     * @param proyeccion Proyeccion a añadir.
     */
    public void addProyeccion(@Valid Proyeccion proyeccion) {
        if(proyeccion == null) throw new IllegalArgumentException("La proyección no puede ser nula.");

        if(!this.proyecciones.contains(proyeccion)) {
            proyeccion.setSesion(this);
            this.proyecciones.add(proyeccion);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Sesion sesion = (Sesion) o;
        return Objects.equals(id, sesion.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
