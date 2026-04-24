package com.tfg.API_TFG.core.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(
        name = "bloqueo_butaca",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_bloqueo_sesion_fila_butaca",
                        columnNames = {"num_sala", "pelicula_id", "horario_sesion", "fila", "butaca"}
                )
        },
        indexes = {
                @Index(name = "ix_bloqueo_expira", columnList = "expira"),
                @Index(name = "ix_bloqueo_token", columnList = "token")
        }
)
public class BloqueoButaca {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumns(value = {
            @JoinColumn(name = "num_sala", referencedColumnName = "num_sala", nullable = false),
            @JoinColumn(name = "pelicula_id", referencedColumnName = "pelicula_id", nullable = false),
            @JoinColumn(name = "horario_sesion", referencedColumnName = "horario_sesion", nullable = false)
    }, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Sesion sesion;

    @Column(nullable = false)
    private int fila;

    @Column(nullable = false)
    private int butaca;

    @Column(nullable = false, length = 64)
    private String token;

    @Column(name = "expira", nullable = false)
    private LocalDateTime expira;

    public BloqueoButaca() {}

    public UUID getId() { return id; }

    public Sesion getSesion() { return sesion; }

    public void setSesion(Sesion sesion) { this.sesion = sesion; }

    public int getFila() { return fila; }

    public void setFila(int fila) { this.fila = fila; }

    public int getButaca() { return butaca; }

    public void setButaca(int butaca) { this.butaca = butaca; }

    public String getToken() { return token; }

    public void setToken(String token) { this.token = token; }

    public LocalDateTime getExpira() { return expira; }

    public void setExpira(LocalDateTime expira) { this.expira = expira; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BloqueoButaca that = (BloqueoButaca) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hashCode(id); }
}