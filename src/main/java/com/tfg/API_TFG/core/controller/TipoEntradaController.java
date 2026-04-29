package com.tfg.API_TFG.core.controller;

import com.tfg.API_TFG.core.dto.CreateTipoEntradaDTO;
import com.tfg.API_TFG.core.dto.TipoEntradaDTO;
import com.tfg.API_TFG.core.entity.TipoEntrada;
import com.tfg.API_TFG.core.service.interfaces.TipoEntradaService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tipo-entrada")
@Tag(name = "Tipos de entrada", description = "API para los tipos de entradas")
public class TipoEntradaController {

    private final TipoEntradaService tipoEntradaService;

    @Autowired
    public TipoEntradaController(TipoEntradaService tipoEntradaService) {
        this.tipoEntradaService = tipoEntradaService;
    }

    @Operation(
            summary = "Obtener todos los tipos de entradas",
            description = "Devuelve una lista con todos los tipos de entradas."
    )
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tipos de entradas obtenidas",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TipoEntradaDTO.class))
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<TipoEntradaDTO>> getAll() {
        return ResponseEntity.ok(tipoEntradaService.getAll());
    }

    @Operation(
                summary = "Devuelve un tipo de entrada por su ID",
                description = "Devuelve la información de un tipo de entrada buscándolo por su id"
        )
        @ApiResponses( value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "El tipo de entrada fue encontrado",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = TipoEntradaDTO.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "Datos inválidos.",
                        content = @Content(
                                mediaType = "application/json",
                                examples = @ExampleObject(
                                        name = "DatosInválidos",
                                        summary = "Ejemplo del error devuelto si el ID es inválido.",
                                        value = """
                                                    {
                                                        "id": "El ID no puede ser nulo",
                                                    }
                                                    """
                                )
                        )
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Tipo de entrada no existente",
                        content = @Content(
                                mediaType = "application/json",
                                examples = @ExampleObject(
                                        name = "ObjetoNoExistente",
                                        summary = "Ejemplo del error devuelto si no existe ese tipo de entrada",
                                        value = """
                                                    {
                                                        "EntityNotFoundException": "No existe el tipo de entrada con ID 1"
                                                    }
                                                    """
                                )
                        )
                )
        })
    @GetMapping("/{id}")
    public ResponseEntity<TipoEntradaDTO> getById(
            @Parameter(description = "ID del tipo de entrada a buscar", example = "1")
            @PathVariable int id) {
        return ResponseEntity.ok(tipoEntradaService.getById(id));
    }

    @Operation(
                summary = "Crea un nuevo tipo de entrada",
                description = "Crea un nuevo tipo de entrada. NO verifica que exista ya uno con el mismo nombre o precio"
        )
        @ApiResponses( value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "El tipo de entrada fue creado con éxito",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = TipoEntradaDTO.class)
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
                                                        "nombre": "El nombre no pude tener más de 25 caracteres.",
                                                        "precio": "El precio no puede ser inferior a 1"
                                                    }
                                                    """
                                )
                        )
                )
        })
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<TipoEntradaDTO> createTipoEntrada(
            @Parameter(description = "Datos del tipo de entrada a crear")
            @RequestBody @Valid CreateTipoEntradaDTO createTipoEntradaDTO
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tipoEntradaService.createTipoEntrada(createTipoEntradaDTO));
    }

    @Operation(
                summary = "Actualiza los datos de un tipo de entrada",
                description = "Actualiza todos los datos menos el ID de un tipo de entrada"
        )
        @ApiResponses( value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "El tipo de entrada fue actualizado con éxito",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = TipoEntradaDTO.class)
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
                                                        "nombre": "El nombre no pude tener más de 25 caracteres.",
                                                        "precio": "El precio no puede ser inferior a 1"
                                                    }
                                                    """
                                )
                        )
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Tipo de entrada no existente no existente",
                        content = @Content(
                                mediaType = "application/json",
                                examples = @ExampleObject(
                                        name = "ObjetoNoExistente",
                                        summary = "Ejemplo del error devuelto si no existe ese tipo de entrada",
                                        value = """
                                                    {
                                                        "EntityNotFoundException": "No existe el tipo de entrada con ID 1"
                                                    }
                                                    """
                                )
                        )
                )
        })
    @PutMapping
    public ResponseEntity<TipoEntradaDTO> updateTipoEntrada(
            @Parameter(description = "Datos del tipo de entrada a actualizar")
            @RequestBody @Valid TipoEntradaDTO tipoEntradaDTO
    ) {
        return ResponseEntity.ok(tipoEntradaService.updateTipoEntrada(tipoEntradaDTO));
    }

    @Operation(
                summary = "Elimina un tipo de entrada",
                description = "Elimina un tipo de entrada por el ID dado."
        )
        @ApiResponses( value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "El tipo de entrada fue eliminado con éxito.",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = TipoEntradaDTO.class)
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
                                                        "id": "El ID debe ser un entero."
                                                    }
                                                    """
                                )
                        )
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Tipo de entrada no existente",
                        content = @Content(
                                mediaType = "application/json",
                                examples = @ExampleObject(
                                        name = "ObjetoNoExistente",
                                        summary = "Ejemplo del error devuelto si no existe ese tipo de entrada",
                                        value = """
                                                    {
                                                        "EntityNotFoundException": "No existe el tipo de entrada con ID 1"
                                                    }
                                                    """
                                )
                        )
                )
        })
    @DeleteMapping("/{id}")
    public ResponseEntity<TipoEntradaDTO> deleteTipoEntrada(
            @Parameter(description = "ID del tipo de entrada a eliminar")
            @PathVariable int id
    ) {
        return ResponseEntity.ok(tipoEntradaService.deleteTipoEntrada(id));
    }
}
