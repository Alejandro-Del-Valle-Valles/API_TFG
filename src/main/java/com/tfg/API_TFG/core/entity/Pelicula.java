package com.tfg.API_TFG.core.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
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

    @Size(max = 511, message = "La URL de la imagen no puede ser superior a 511 cracateres.")
    @Column(length = 511, unique = true)
    private String portada;

    @NotNull(message = "La duración de la película no puede ser nula.")
    @Positive(message = "La duración de la película no puede ser 0 o negativa.")
    @Column(precision = 4, scale = 2)
    private BigDecimal duracion;

    @PositiveOrZero(message = "La calificación de edad de la película no puede ser negativa.")
    private Integer calificacionEdad;

    @OneToMany(mappedBy = "pelicula", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<@Valid Credito> creditos = new ArrayList<>();

    public Pelicula() { }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPortada() {
        return portada;
    }

    public void setPortada(String portada) {
        this.portada = portada;
    }

    public BigDecimal getDuracion() {
        return duracion;
    }

    /**
     * Comprueba que los minutos no sean superiores a 59
     * @param duracion BigDecimal de la duración de la película.
     */
    public void setDuracion(BigDecimal duracion) {
        if (duracion != null) {
            int minutos = duracion.movePointRight(2).remainder(BigDecimal.valueOf(100)).intValue();

            if (minutos > 59)
                throw new IllegalArgumentException("Los minutos no pueden ser mayores a 59 (Formato HH.mm)");
        }
        this.duracion = duracion;
    }

    public Integer getCalificacionEdad() {
        return calificacionEdad;
    }

    public void setCalificacionEdad(Integer calificacionEdad) {
        this.calificacionEdad = calificacionEdad;
    }

    public List<Credito> getCreditos() {
        return creditos;
    }

    public void setCreditos(List<@Valid Credito> creditos) {
        this.creditos = creditos;
    }

    /**
     * Añade el crédito y establece la relación entre Película, Crédito y Participante
     * @param credito Credito a añadir
     */
    public void addCredito(@Valid Credito credito) {
        if (credito == null) throw new IllegalArgumentException("El crédito no puede ser nulo.");

        credito.setPelicula(this);
        this.creditos.add(credito);

        if (credito.getParticipante() != null && !credito.getParticipante().getCreditos().contains(credito)) {
            credito.getParticipante().getCreditos().add(credito);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Pelicula pelicula = (Pelicula) o;
        return Objects.equals(id, pelicula.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
