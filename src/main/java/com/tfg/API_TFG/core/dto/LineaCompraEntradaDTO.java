package com.tfg.API_TFG.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(
        description = "Línea de compra para entrada",
        example = """
    {
      "type": "ENTRADA",
      "numero": 2,
      "entrada": {
        "numSala": 1,
        "peliculaId": "11111111-1111-1111-1111-111111111111",
        "horario": "2026-06-21T18:30",
        "fila": 5,
        "butaca": 8
      }
    }
    """
)
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
