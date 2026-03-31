package com.tfg.API_TFG.core.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Objects;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = LineaCompraProductoCreateDTO.class, name = "PRODUCTO"),
        @JsonSubTypes.Type(value = LineaCompraEntradaDTO.class, name = "ENTRADA"),
        @JsonSubTypes.Type(value = LineaCompraProductoDTO.class, name = "PRODUCTO")
})
@Schema(
        description = "Línea de compra polimórfica. Puede ser para una entrada o un producto.",
        discriminatorProperty = "type",
        oneOf = {LineaCompraProductoCreateDTO.class, LineaCompraEntradaDTO.class, LineaCompraProductoDTO.class}
)
public abstract class LineaCompraDTO {

    @Schema(
            description = "Numero de la línea",
            example = "1"
    )
    @Positive(message = "El número de la linea no puede ser inferior a 1")
    @NotNull(message = "El número no puede ser nulo.")
    private Integer numero;

    public LineaCompraDTO(Integer numero) {
        this.numero = numero;
    }

    public Integer getNumero() { return numero; }

    public void setNumero(Integer numero) { this.numero = numero; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LineaCompraDTO that = (LineaCompraDTO) o;
        return Objects.equals(numero, that.numero);
    }

    @Override
    public int hashCode() { return Objects.hashCode(numero); }
}
