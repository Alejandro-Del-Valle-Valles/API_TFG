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

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean tresD = false;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean vose = false;

    @ManyToOne
    @JoinColumn(name = "num_sala", referencedColumnName = "numSala", insertable = false, updatable = false)
    private @Valid Sala sala;

    @ManyToOne
    @JoinColumn(name = "pelicula_id", referencedColumnName = "id", insertable = false, updatable = false)
    private @Valid Pelicula pelicula;

    @OneToMany(mappedBy = "sesion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<@Valid BloqueoButaca> bloqueoButacas = new ArrayList<>();

    @OneToMany(mappedBy = "sesion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<@Valid Entrada> entradas = new ArrayList<>();

    public Sesion() { }

    public SesionId getId() { return id; }

    public void setId(SesionId id) { this.id = id; }

    public boolean isTresD() { return tresD; }

    public void setTresD(boolean es3D) { this.tresD = es3D; }

    public boolean isVose() { return vose; }

    public void setVose(boolean esVose) { this.vose = esVose; }

    public Sala getSala() { return sala; }

    public void setSala(@Valid Sala sala) { this.sala = sala; }

    public Pelicula getPelicula() { return pelicula; }

    public void setPelicula(Pelicula pelicula) { this.pelicula = pelicula; }

    public LocalDateTime getHorario() { return id.getHorarioSesion(); }

    public void setHorario(LocalDateTime horario) { this.id.setHorarioSesion(horario); }

    public List<Entrada> getEntradas() { return entradas; }

    public void setEntradas(List<@Valid Entrada> entradas) { this.entradas = entradas; }

    public void addEntrada(@Valid Entrada entrada) {
        if(entrada == null) throw new IllegalArgumentException("La entrada no puede ser nula");
        if(!this.entradas.contains(entrada)) {
            entrada.setSesion(this);
            this.entradas.add(entrada);
        }
    }

    public List<BloqueoButaca> getBloqueoButacas() { return bloqueoButacas; }

    public void setBloqueoButacas(List<@Valid BloqueoButaca> bloqueoButacas) { this.bloqueoButacas = bloqueoButacas; }

    public void addBloqueButaca(@Valid BloqueoButaca butaca) {
        if(butaca == null) throw new IllegalArgumentException("La butaca no puede ser nula");
        if(!this.bloqueoButacas.contains(butaca)) {
            butaca.setSesion(this);
            this.bloqueoButacas.add(butaca);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Sesion that = (Sesion) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hashCode(id); }
}
