package com.tfg.API_TFG.core.enums;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Géneros disponibles para películas
 */
@Schema(
        description = "Géneros de películas.",
        enumAsRef = true
)
public enum GeneroPeliculas {
    ACCION, TERROR, CIENCIA_FICCION, COMEDIA, ROMANTICA, CINE_NEGRO, DRAMA, HISTORICA, BELICA, POLICIACA,
    DOCUMENTAL, MUSICAL, INFANTIL, SUSPENSE, WESTERN_CLASICO, MAGICO, AVENTURA, FANTASIA
}
