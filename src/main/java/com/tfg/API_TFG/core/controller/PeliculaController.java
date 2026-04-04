package com.tfg.API_TFG.core.controller;

import com.tfg.API_TFG.core.dto.PeliculaCompletoDTO;
import com.tfg.API_TFG.core.dto.PeliculaCreateDTO;
import com.tfg.API_TFG.core.dto.PeliculaDTO;
import com.tfg.API_TFG.core.service.interfaces.PeliculaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pelicula")
@Tag(name = "Peliculas", description = "API para la administración de películas.")
public class PeliculaController {
    private final PeliculaService peliculaService;

    @Autowired
    public PeliculaController(PeliculaService peliculaService) {
        this.peliculaService = peliculaService;
    }

    @Operation(
            summary = "Obtener todas las películas con su info básica.",
            description = "Devuelve una lista con todas las películas con la información básica."
    )
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Registros obtenidos",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PeliculaDTO.class))
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<PeliculaDTO>> getAllBasic() {
        return ResponseEntity.ok(peliculaService.getAll());
    }

    @Operation(
            summary = "Obtener todas las películas con la info de sus participantes.",
            description = "Devuelve una lista con todas las películas y sus participantes junto a sus roles."
    )
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Registros obtenidos",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PeliculaCompletoDTO.class))
                    )
            )
    })
    @GetMapping("/completo")
    public ResponseEntity<List<PeliculaCompletoDTO>> getAllCompleto() {
        return ResponseEntity.ok(peliculaService.getAllCompleto());
    }

    @Operation(
            summary = "Obtener todas las películas con la info básica que contenga una palabra.",
            description = "Devuelve una lista con todas las películas que ocntenga la palabra buscada independientemente del case."
    )
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Registros obtenidos",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PeliculaDTO.class))
                    )
            )
    })
    @GetMapping("/basico/{nombre}")
    public ResponseEntity<List<PeliculaDTO>> getAllBasicByNombre(
            @Parameter(description = "Palabra que contiene el nombre. No importa el case.")
            @PathVariable String nombre
    ) {
        return ResponseEntity.ok(peliculaService.getByNombreContainingIgonreCase(nombre));
    }

    @Operation(
            summary = "Obtener todas las películas con la info de sus participantes.",
            description = "Devuelve una lista con todas las películas y sus participantes junto a sus roles en base a la palabra buscada independientemente del case."
    )
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Registros obtenidos",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PeliculaCompletoDTO.class))
                    )
            )
    })
    @GetMapping("/completo/{nombre}")
    public ResponseEntity<List<PeliculaCompletoDTO>> getAllCompletoByNombre(
            @Parameter(description = "Palabra que contiene el nombre. No importa el case.")
            @PathVariable String nombre
    ) {
        return ResponseEntity.ok(peliculaService.getCompletoByNombreContainingIgnoreCase(nombre));
    }

    @Operation(
                summary = "Busca y devuelve una película por su ID",
                description = "Busca si existe la película con el ID especificado y la devuelve si existe con toda su información."
        )
        @ApiResponses( value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "La película fue encontrada.",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = PeliculaCompletoDTO.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "Datos inválidos.",
                        content = @Content(
                                mediaType = "application/json",
                                examples = @ExampleObject(
                                        name = "DatosInválidos",
                                        summary = "Ejemplo del error devuelto si algún dato es erróneo.",
                                        value = """
                                                    {
                                                        "id": "El ID no es correcto",
                                                    }
                                                    """
                                )
                        )
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Objeto no existente",
                        content = @Content(
                                mediaType = "application/json",
                                examples = @ExampleObject(
                                        name = "ObjetoNoExistente",
                                        summary = "Ejemplo del error devuelto si no existe esa película",
                                        value = """
                                                    {
                                                        "EntityNotFoundException": "No existe la película con ID: c5363d99-8ed5-4694-94be-20141bf1cc2f"
                                                    }
                                                    """
                                )
                        )
                )
        })
    @GetMapping("/id/{id}")
    public ResponseEntity<PeliculaCompletoDTO> getById(
            @Parameter(description = "UUID de la película buscada")
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(peliculaService.getCompletoById(id));
    }

    @Operation(
                summary = "Crea una nueva película.",
                description = "Crea una nueva película y establece las relaciones con participantes."
        )
        @ApiResponses( value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "La película fue creada con éxito.",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = PeliculaCompletoDTO.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "Datos inválidos.",
                        content = @Content(
                                mediaType = "application/json",
                                examples = @ExampleObject(
                                        name = "DatosInválidos",
                                        summary = "Ejemplo del error devuelto si algún dato es erróneo.",
                                        value = """
                                                    {
                                                        "nombre": "El nombre no puede contener más de 51 caracteres.",
                                                        "duracion": "La duración no puede ser negativa.",
                                                        "url": "La URL ya está en uso"
                                                    }
                                                    """
                                )
                        )
                )
        })
    @PostMapping
    public ResponseEntity<PeliculaCompletoDTO> createPelicula(
            @Parameter(description = "DTO con los datos y participantes de la nueva película.")
            @RequestBody @Valid PeliculaCreateDTO peliculaCreateDTO
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(peliculaService.createPelicula(peliculaCreateDTO));
    }

    @Operation(
                summary = "Actualiza una película ya existente.",
                description = "Actualiza los datos de la película y actualiza las relaciones con los participantes. Solo va a contener aquellos que se pasen con el DTO."
        )
        @ApiResponses( value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "La película fue actualizada con éxito.",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = PeliculaCreateDTO.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "Datos inválidos.",
                        content = @Content(
                                mediaType = "application/json",
                                examples = @ExampleObject(
                                        name = "DatosInválidos",
                                        summary = "Ejemplo del error devuelto si algún dato es erróneo.",
                                        value = """
                                                    {
                                                        "nombre": "El nombre no puede contener más de 51 caracteres.",
                                                        "duracion": "La duración no puede ser negativa.",
                                                        "url": "La URL ya está en uso"
                                                    }
                                                    """
                                )
                        )
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Objeto no existente",
                        content = @Content(
                                mediaType = "application/json",
                                examples = @ExampleObject(
                                        name = "ObjetoNoExistente",
                                        summary = "Ejemplo del error devuelto si no existe la película",
                                        value = """
                                                    {
                                                        "EntityNotFoundException": "No existe la película con ID af58928b-7544-457b-9fc1-79eff8046b1f"
                                                    }
                                                    """
                                )
                        )
                )
        })
    @PutMapping("/{id}")
    public ResponseEntity<PeliculaCompletoDTO> updatePelicula(
            @Parameter(description = "UUID de la película a actualizar")
            @PathVariable UUID id,
            @Parameter(description = "DTO con los datos y participantes de la película.")
            @RequestBody PeliculaCreateDTO pleiculaUpdateDTO
    ) {
        return ResponseEntity.ok(peliculaService.updatePeliculaCompleto(id, pleiculaUpdateDTO));
    }

    @Operation(
                summary = "Elimina un registro",
                description = "Elimina un registro por el ID dado."
        )
        @ApiResponses( value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "El registro fue eliminado con éxito.",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = PeliculaDTO.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "Datos inválidos.",
                        content = @Content(
                                mediaType = "application/json",
                                examples = @ExampleObject(
                                        name = "DatosInválidos",
                                        summary = "Ejemplo del error devuelto si algún dato es erróneo.",
                                        value = """
                                                    {
                                                        "id": "El ID debe ser un UUID."
                                                    }
                                                    """
                                )
                        )
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Objeto no existente",
                        content = @Content(
                                mediaType = "application/json",
                                examples = @ExampleObject(
                                        name = "ObjetoNoExistente",
                                        summary = "Ejemplo del error devuelto si no existe esa película.",
                                        value = """
                                                    {
                                                        "EntityNotFoundException": "No existe el la película con el ID af58928b-7544-457b-9fc1-79eff8046b1f"
                                                    }
                                                    """
                                )
                        )
                )
        })
    @DeleteMapping("/{id}")
    public ResponseEntity<PeliculaDTO> deletePelicula(
            @Parameter(description = "ID de la película a eliminar")
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(peliculaService.deletePelicula(id));
    }
}
