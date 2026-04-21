package com.tfg.API_TFG.core.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Baner {

    @Id
    private UUID id;

    @MapsId
    @ManyToOne
    @JoinColumn(name = "pelicula_id", nullable = false)
    private @Valid Pelicula pelicula;

    @NotNull(message = "La URL no puede ser nula.")
    @NotEmpty(message = "La URL no puede estar vacía.")
    @Size(max = 511, message = "La URL de la imagen no puede ser superior a 511 cracateres.")
    @Column(length = 511, unique = true)
    private String url;

    @NotNull(message = "La fecha de inicio no puede ser nula")
    private LocalDate empieza;

    @NotNull(message = "La fecha de fin no puede ser nula")
    private LocalDate termina;

    public Baner() { }

    public UUID getId() { return id; }

    public void setId(UUID id) { this.id = id; }

    public Pelicula getPelicula() { return pelicula; }

    public void setPelicula(Pelicula pelicula) { this.pelicula = pelicula;}

    public String getUrl() { return url; }

    public void setUrl(String url) { this.url = url; }

    public LocalDate getEmpieza() { return empieza; }

    public void setEmpieza(LocalDate empieza) { this.empieza = empieza; }

    public LocalDate getTermina() { return termina; }

    public void setTermina(LocalDate termina) { this.termina = termina; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Baner baner = (Baner) o;
        return Objects.equals(id, baner.id) && Objects.equals(pelicula, baner.pelicula);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pelicula);
    }
}
