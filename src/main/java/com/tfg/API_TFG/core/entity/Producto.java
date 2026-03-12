package com.tfg.API_TFG.core.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "El nombre del producto no puede ser nulo.")
    @NotBlank(message = "El nombre del producto no puede estar vacío.")
    @Size(max = 50, message = "El nombre no puede contener más de 50 caracteres.")
    @Column(length = 50, unique = true)
    private String nombre;

    @NotNull(message = "El precio no puede ser nulo.")
    @Positive(message = "El precio no puede ser 0 o negativo.")
    @Column(precision = 5, scale = 2)
    private BigDecimal precio;

    @ManyToMany
    @JoinTable(
            name = "producto_alergeno",
            joinColumns = @JoinColumn(name = "alergeno_id"),
            inverseJoinColumns = @JoinColumn(name = "producto_id")
    )
    private List<@Valid Alergeno> alergenos;

    @OneToMany(mappedBy = "producto")
    private List<@Valid LineaCompra> lineaCompras;

    public Producto() { }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public List<Alergeno> getAlergenos() {
        return alergenos;
    }

    public void setAlergenos(List<@Valid Alergeno> alergenos) {
        this.alergenos = alergenos;
    }

    public List<LineaCompra> getLineaCompras() {
        return lineaCompras;
    }

    public void setLineaCompras(List<@Valid LineaCompra> lineaCompras) {
        this.lineaCompras = lineaCompras;
    }

    /**
     * Añade la línea de compra si no la tiene ya y establece la relación entre ambos.
     * @param lineaCompra Linea de compra a añadir
     */
    public void addLineaCompra(@Valid LineaCompra lineaCompra) {
        if(lineaCompra == null) throw new IllegalArgumentException("La línea de compra no puede ser nula.");
        if(!this.lineaCompras.contains(lineaCompra)) {
            lineaCompra.setProducto(this);
            this.lineaCompras.add(lineaCompra);
        }
    }

    /**
     * Añade un nuevo alérgeno al producto y establece la relación.
     * @param alergeno Alergeno a añadir.
     */
    public void addAlergeno(@Valid Alergeno alergeno) {
        if(alergeno == null) throw new IllegalArgumentException("El alérgeno no puede ser nulo.");
        if(!alergeno.getProductos().contains(this)) {
            alergeno.getProductos().add(this);
            this.alergenos.add(alergeno);
        }
    }

    /**
     * Elimina un alérgeno y su relación.
     * @param alergeno Alergeno a eliminar de la relación.
     */
    public void removeAlergeno(@Valid Alergeno alergeno) {
        if(alergeno == null) throw new IllegalArgumentException("El alérgeno no puede ser nulo.");
        alergeno.getProductos().remove(this);
        this.alergenos.remove(alergeno);
    }

    @PreRemove
    private void removeProductoFromAlergenos() {
        this.alergenos.forEach(alergeno ->
                alergeno.getProductos().remove(this)
        );
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return Objects.equals(id, producto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
