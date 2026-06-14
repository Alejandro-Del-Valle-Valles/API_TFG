package com.tfg.API_TFG.core.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Los códigos serán aplicables a este tipo de productos",
        enumAsRef = true
)

public enum CondicionDescuento {
    COMIDA, PELICULA, TODOS
}
