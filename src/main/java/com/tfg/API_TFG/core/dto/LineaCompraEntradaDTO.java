package com.tfg.API_TFG.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Información de una línea de compra para una entrada.")
public class LineaCompraEntradaDTO extends LineaCompraDTO{

    @Schema(
            description = "Entrada de la línea de compra",
            implementation = EntradaDTO.class
    )
    @NotNull(message = "La entrada no puede ser nula.")
    private EntradaDTO entrada;

    public LineaCompraEntradaDTO(Integer numero, EntradaDTO entrada) {
        super(numero);
        this.entrada = entrada;
    }

    public EntradaDTO getEntrada() {
        return entrada;
    }

    public void setEntrada(EntradaDTO entrada) {
        this.entrada = entrada;
    }
}
