package com.tfg.API_TFG.core.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "El correo no puede ser nulo.")
    @NotBlank(message = "El correo no puede star vacío.")
    @Size(max = 100, message = "El correo no puede contener más de 100 caracteres.")
    @Column(length = 100, unique = true)
    private String correo;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cuenta cuenta;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<@Valid Compra> compras;

    public Usuario() { }

    public UUID getId() { return id; }

    public void setId(UUID id) { this.id = id; }

    public String getCorreo() { return correo; }

    public void setCorreo(String correo) { this.correo = correo; }

    public Cuenta getCuenta() { return cuenta; }

    public void setCuenta(Cuenta cuenta) {
        if (cuenta == null) this.cuenta.setUsuario(null);
        else cuenta.setUsuario(this);

        this.cuenta = cuenta;
    }

    public List<Compra> getCompras() { return compras; }

    public void setCompras(List<@Valid Compra> compras) { this.compras = compras; }

    /**
     * Añade la compra a la lista si no existe ya y establece la relación.
     * @param compra Compra a añadir
     */
    public void addCompra(@Valid Compra compra) {
        if(compra == null) throw new IllegalArgumentException("La compra no puede ser nula.");
        if(!this.compras.contains(compra)) {
            compra.setUsuario(this);
            this.compras.add(compra);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() { return Objects.hashCode(id); }
}
