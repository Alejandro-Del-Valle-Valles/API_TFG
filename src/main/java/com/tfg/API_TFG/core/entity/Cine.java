package com.tfg.API_TFG.core.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Cine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El nombre del cine no puede ser nulo.")
    @NotBlank(message = "El nombre del cine no puede estar vacío.")
    @Size(max = 50, message = "El nombre no puede contener más de 50 caracteres.")
    @Column(length = 50)
    private String nombre;

    @NotNull(message = "La latitud del cine no puede ser nula.")
    @Column(precision = 9, scale = 6)
    private BigDecimal latitud;

    @NotNull(message = "La longitud del cine no puede ser nula.")
    @Column(precision = 9, scale = 6)
    private BigDecimal longitud;

    @OneToMany(mappedBy = "cine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<@Valid Sala> salas = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "cine-producto",
            joinColumns = @JoinColumn(name = "producto_id"),
            inverseJoinColumns = @JoinColumn(name = "cine_id")
    )
    private List<@Valid Producto> productos;

    public Cine() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getLatitud() {
        return latitud;
    }

    public void setLatitud(BigDecimal latitud) {
        this.latitud = latitud;
    }

    public BigDecimal getLongitud() {
        return longitud;
    }

    public void setLongitud(BigDecimal longitud) {
        this.longitud = longitud;
    }

    public List<Sala> getSalas() {
        return salas;
    }

    public void setSalas(List<@Valid Sala> salas) {
        this.salas = salas;
    }

    /**
     * Añade una sala al cine y establece automaticamente la relación entre ambas.
     * @param sala Sala a añadir
     */
    public void addSala(@Valid Sala sala) {
        if(sala == null) throw new IllegalArgumentException("La sala no puede ser nula.");
        sala.setCine(this);
        this.salas.add(sala);
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<@Valid Producto> productos) {
        this.productos = productos;
    }

    /**
     * Añade un producto a la lista de productos y establece la relación entre ambas.
     * @param producto Producto a añadir
     */
    public void addProducto(@Valid Producto producto) {
        if(producto == null) throw new IllegalArgumentException("El producto no puede ser nulo.");
        if(!this.productos.contains(producto)) {
            this.productos.add(producto);
            producto.getCines().add(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Cine cine = (Cine) o;
        return Objects.equals(id, cine.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
