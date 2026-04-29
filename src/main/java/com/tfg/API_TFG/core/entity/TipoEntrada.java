package com.tfg.API_TFG.core.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class TipoEntrada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "El nombre del producto no puede ser nulo.")
    @NotBlank(message = "El nombre del producto no puede estar vacío.")
    @Size(max = 20, message = "El nombre no puede contener más de 20 caracteres.")
    @Column(length = 20, unique = true)
    private String nombre;

    @NotNull(message = "El nombre del producto no puede ser nulo.")
    @NotBlank(message = "El nombre del producto no puede estar vacío.")
    @Size(max = 50, message = "El nombre no puede contener más de 50 caracteres.")
    private String descripcion;

    @NotNull(message = "El precio no puede ser nulo.")
    @Positive(message = "El precio no puede ser 0 o negativo.")
    @Column(precision = 5, scale = 2)
    private BigDecimal precio;

    @OneToMany(mappedBy = "tipo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<@Valid Entrada> entradas = new ArrayList<>();

    public TipoEntrada() { }

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }

    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal getPrecio() { return precio; }

    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public List<Entrada> getEntradas() { return entradas; }

    public void setEntradas(List<Entrada> entradas) { this.entradas = entradas; }

    public void addEntrada(@Valid Entrada entrada) {
        if(entrada == null) throw new IllegalArgumentException("La entrada no puede ser nula");
        if(!this.entradas.contains(entrada)) {
            entrada.setTipo(this);
            this.entradas.add(entrada);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TipoEntrada that = (TipoEntrada) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hashCode(id); }
}
