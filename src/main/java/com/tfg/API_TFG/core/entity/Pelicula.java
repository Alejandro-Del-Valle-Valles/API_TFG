package com.tfg.API_TFG.core.entity;

import com.tfg.API_TFG.core.enums.GeneroPeliculas;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Pelicula {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "El nombre de la película no puede ser nulo.")
    @NotBlank(message = "El nombre de la película no puede estar vacío.")
    @Size(max = 50, message = "El nombre de la película no puede tener más de 50 caracteres.")
    @Column(length = 50)
    private String nombre;

    @Size(max = 511, message = "La descripción no puede superar los 511 caracteres.")
    @Column(columnDefinition = "VARCHAR(511) DEFAULT 'Sin Descripción'")
    private String descripcion = "Sin Descripción";

    @NotNull(message = "La película debe contener un género")
    @Enumerated(EnumType.STRING)
    @Column(length = 25)
    private GeneroPeliculas genero;

    @Size(max = 511, message = "La URL de la imagen no puede ser superior a 511 cracateres.")
    @Column(length = 511, unique = true)
    private String portada;

    @NotNull(message = "La duración de la película no puede ser nula.")
    private LocalTime duracion;

    @PositiveOrZero(message = "La calificación de edad de la película no puede ser negativa.")
    private Integer calificacionEdad = 0;

    /**
     * True en cartelera, False estreno, null no está en cartelera
     */
    @Column(columnDefinition = "BOOLEAN DEFAULT NULL")
    private Boolean enCartelera;

    @OneToMany(mappedBy = "pelicula", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<@Valid Credito> creditos = new ArrayList<>();

    @OneToMany(mappedBy = "pelicula", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<@Valid Sesion> sesiones = new ArrayList<>();

    @OneToMany(mappedBy = "pelicula", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<@Valid Baner> baners = new ArrayList<>();

    public Pelicula() { }

    public UUID getId() { return id; }

    public void setId(UUID id) { this.id = id; }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }

    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public GeneroPeliculas getGenero() { return genero; }

    public void setGenero(GeneroPeliculas genero) { this.genero = genero; }

    public String getPortada() { return portada; }

    public void setPortada(String portada) { this.portada = portada; }

    public LocalTime getDuracion() { return duracion; }

    public void setDuracion(LocalTime duracion) { this.duracion = duracion; }

    public Integer getCalificacionEdad() { return calificacionEdad; }

    public void setCalificacionEdad(Integer calificacionEdad) { this.calificacionEdad = calificacionEdad; }

    public Boolean isEnCartelera() { return enCartelera; }

    public void setEnCartelera(Boolean enCartelera) { this.enCartelera = enCartelera; }

    public List<Credito> getCreditos() { return creditos; }

    public void setCreditos(List<@Valid Credito> creditos) { this.creditos = creditos; }

    /**
     * Añade el crédito y establece la relación entre Película, Crédito y Participante
     * @param credito Credito a añadir
     */
    public void addCredito(@Valid Credito credito) {
        if (credito == null) throw new IllegalArgumentException("El crédito no puede ser nulo.");

        if(!this.creditos.contains(credito)) {
            credito.setPelicula(this);
            this.creditos.add(credito);
        }

        if (credito.getParticipante() != null && !credito.getParticipante().getCreditos().contains(credito)) {
            credito.getParticipante().getCreditos().add(credito);
        }
    }

    public List<Sesion> getSesiones() {
        return sesiones;
    }

    public void setSesiones(List<Sesion> proyecciones) {
        this.sesiones = proyecciones;
    }

    public void addSesion(@Valid Sesion sesion) {
        if(sesion == null) throw new IllegalArgumentException("La sesión no puede ser nula.");

        if(!this.sesiones.contains(sesion)) {
            sesion.setPelicula(this);
            this.sesiones.add(sesion);
        }
    }

    public Boolean getEnCartelera() { return enCartelera; }

    public List<Baner> getBaners() { return baners; }

    public void setBaners(List<@Valid Baner> baners) { this.baners = baners; }

    public void addBaner(@Valid Baner baner) {
        if (baner == null) throw new IllegalArgumentException("El baner no puede ser nulo.");
        if (baner.getPelicula() != null && baner.getPelicula() != this) baner.getPelicula().removeBaner(baner);
        if (!this.baners.contains(baner)) this.baners.add(baner);
        baner.setPelicula(this);
    }

    public void removeBaner(@Valid Baner baner) {
        if (baner == null) return;
        if (this.baners.remove(baner)) baner.setPelicula(null);
    }

    /**
     * Limpia las relaciones antes de eliminar la película.
     * Aunque @PreRemove de Credito ya lo hace, esto asegura limpieza completa.
     */
    @PreRemove
    private void limpiarRelaciones() {
        for (Credito credito : new ArrayList<>(this.creditos)) {
            if (credito.getParticipante() != null)
                credito.getParticipante().getCreditos().remove(credito);
        }
        this.creditos.clear();
        for (Sesion sesion : new ArrayList<>(this.sesiones)) {
            if (sesion.getSala() != null)
                sesion.getSala().getSesiones().remove(sesion);
        }
        this.sesiones.clear();
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Pelicula pelicula = (Pelicula) o;
        return Objects.equals(id, pelicula.id);
    }

    @Override
    public int hashCode() { return Objects.hashCode(id); }
}
