package com.tfg.API_TFG.core.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Participante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "El nombre del participante no puede ser nulo.")
    @NotBlank(message = "El nombre del participante no puede estar en blanco.")
    @Size(max = 50, message = "El nombre no puede contener más de 50 caracteres.")
    @Column(length = 50)
    private String nombre;

    @OneToMany(mappedBy = "participante", cascade = CascadeType.ALL)
    private List<@Valid Credito> creditos = new ArrayList<>();

    public Participante() { }

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public List<Credito> getCreditos() { return creditos; }

    public void setCreditos(List<@Valid Credito> creditos) { this.creditos = creditos; }

    /**
     * Añade un crédito al participante y establece la relación.
     * @param credito Credito a añadir
     */
    public void addCredito(@Valid Credito credito) {
        if(credito == null) throw new IllegalArgumentException("El crédito no puede ser nulo.");
        credito.setParticipante(this);
        this.creditos.add(credito);

        if (credito.getPelicula() != null && !credito.getPelicula().getCreditos().contains(credito)) {
            credito.getPelicula().getCreditos().add(credito);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Participante that = (Participante) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hashCode(id); }
}
