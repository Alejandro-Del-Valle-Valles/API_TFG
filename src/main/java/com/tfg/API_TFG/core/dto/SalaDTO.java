package com.tfg.API_TFG.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Objects;

@Schema(description = "Infromación de una sala.")
public class SalaDTO {

    @Schema(
            description = "Número de la sala.",
            examples = "1"
    )
    @Positive(message = "El número de la sala no puede ser inferior a 1.")
    @NotNull(message = "La sala debe tener un número, no puede ser nulo.")
    private Integer numero;

    @Schema(
            description = "Aforo de la sala.",
            examples = "100"
    )
    @Positive(message = "El aforo de la sala no pude ser inferior a 1.")
    @NotNull(message = "El aforo de la sala no puede ser nulo.")
    private Integer aforo;

    public SalaDTO(Integer numero, Integer aforo) {
        this.numero = numero;
        this.aforo = aforo;
    }

    public Integer getNumero() { return numero; }

    public void setNumero(Integer numero) { this.numero = numero; }

    public Integer getAforo() { return aforo; }

    public void setAforo(Integer aforo) { this.aforo = aforo; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SalaDTO salaDTO = (SalaDTO) o;
        return Objects.equals(numero, salaDTO.numero);
    }

    @Override
    public int hashCode() { return Objects.hashCode(numero); }
}
