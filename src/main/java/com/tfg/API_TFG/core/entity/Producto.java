package com.tfg.API_TFG.core.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

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

    @ManyToMany(mappedBy = "productos")
    private List<@Valid Cine> cines;

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

    public List<Cine> getCines() {
        return cines;
    }

    public void setCines(List<@Valid Cine> cines) {
        this.cines = cines;
    }

    /**
     * Añade un cine a la lista de cines en los que está disponible y establece la relación.
     * @param cine Cine a añadir.
     */
    public void addCine(@Valid Cine cine) {
        if(cine == null) throw new IllegalArgumentException("El cine no puede ser nulo.");
        if(!this.cines.contains(cine)) {
            this.cines.add(cine);
            cine.getProductos().add(this);
        }
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
        alergeno.getProductos().add(this);
        this.alergenos.add(alergeno);
    }
}
