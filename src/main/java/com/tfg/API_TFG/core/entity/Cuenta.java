package com.tfg.API_TFG.core.entity;

import com.tfg.API_TFG.core.enums.RolUsuario;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Objects;
import java.util.UUID;

@Entity
public class Cuenta {

    @Id
    private UUID id;

    @NotNull(message = "El nombre del usuario no puede ser nulo.")
    @NotBlank(message = "El nombre del usuario no puede estar vacío.")
    @Size(max = 50, message = "El nombre del usuario no puede contener más de 50 caracteres.")
    @Column(length = 50)
    private String nombre;

    @NotNull(message = "La contraseña no puede ser nula.")
    @NotBlank(message = "La contraseña no puede estar vacía.")
    private String contrasena;

    @NotNull(message = "El rol del usuario no puede ser nulo.")
    @Enumerated(EnumType.STRING)
    @Column(length = 25)
    private RolUsuario rol;

    @OneToOne
    @MapsId
    @JoinColumn(name = "usuario_id")
    private @Valid Usuario usuario;

    public Cuenta() { }

    public UUID getId() { return id; }

    public void setId(UUID id) { this.id = id; }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getContrasena() { return contrasena; }

    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public RolUsuario getRol() { return rol; }

    public void setRol(RolUsuario rol) { this.rol = rol; }

    public Usuario getUsuario() { return usuario; }

    public void setUsuario(@Valid Usuario usuario) { this.usuario = usuario; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Cuenta cuenta = (Cuenta) o;
        return Objects.equals(id, cuenta.id);
    }

    @Override
    public int hashCode() { return Objects.hashCode(id); }
}
