package com.tfg.API_TFG.core.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Objects;

@Entity
public class Alergeno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "El nombre del alérgeno no puede ser nulo.")
    @NotBlank(message = "El nombre del alérgeno no puede estar vacío.")
    @Size(max = 25, message = "El nombre no puede contener más de 25 caracteres.")
    @Column(length = 25, unique = true)
    private String nombre;

    @ManyToMany(mappedBy = "alergenos")
    private List<@Valid Producto> productos;

    public Alergeno() { }

    public Alergeno(String nombre) {
        this.nombre = nombre;
    }

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public List<Producto> getProductos() { return productos; }

    public void setProductos(List<@Valid Producto> productos) { this.productos = productos; }

    @PreRemove
    private void removeAlergenoFromProductos() {
        this.productos.forEach(producto ->
                producto.getAlergenos().remove(this)
        );
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Alergeno alergeno = (Alergeno) o;
        return Objects.equals(id, alergeno.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
