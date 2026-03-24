package com.tfg.API_TFG.core.entity;

import com.tfg.API_TFG.core.entity.id.SesionId;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Sesion {

    @EmbeddedId
    private SesionId id;

    @ManyToOne
    @JoinColumn(name = "num_sala", referencedColumnName = "numSala", insertable = false, updatable = false)
    private @Valid Sala sala;

    @ManyToOne
    @JoinColumn(name = "pelicula_id", referencedColumnName = "id", insertable = false, updatable = false)
    private @Valid Pelicula pelicula;

    @NotNull(message = "El horario no puede ser nulo.")
    private LocalDateTime horario;

    @OneToMany(mappedBy = "sesion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<@Valid Entrada> entradas = new ArrayList<>();

    public Sesion() { }

    public SesionId getId() { return id; }

    public void setId(SesionId id) { this.id = id; }

    public Sala getSala() { return sala; }

    public void setSala(@Valid Sala sala) { this.sala = sala; }

    public Pelicula getPelicula() { return pelicula; }

    public void setPelicula(Pelicula pelicula) { this.pelicula = pelicula; }

    public LocalDateTime getHorario() { return horario; }

    public void setHorario(LocalDateTime horario) { this.horario = horario; }

    public List<Entrada> getEntradas() { return entradas; }

    public void setEntradas(List<@Valid Entrada> entradas) { this.entradas = entradas; }

    public void addEntrada(@Valid Entrada entrada) {
        if(entrada == null) throw new IllegalArgumentException("La entrada no puede ser nula");
        if(!this.entradas.contains(entrada)) {
            entrada.setSesion(this);
            this.entradas.add(entrada);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Sesion that = (Sesion) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
