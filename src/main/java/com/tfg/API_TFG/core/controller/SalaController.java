package com.tfg.API_TFG.core.controller;

import com.tfg.API_TFG.core.dto.SalaDTO;
import com.tfg.API_TFG.core.entity.Sala;
import com.tfg.API_TFG.core.service.interfaces.SalaService;
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

@RestController
@RequestMapping("/sala")
@Tag(name = "Salas", description = "API para las salas")
public class SalaController {

    private final SalaService salaService;

    @Autowired
    public SalaController(SalaService salaService) {
        this.salaService = salaService;
    }

    @Operation(
                summary = "Obtiene todas las salas.",
                description = "Devuelve una lista con los datos de todas las salas."
        )
        @ApiResponses( value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Lista con todas las salas.",
                        content = @Content(
                                mediaType = "application/json",
                                array = @ArraySchema(schema = @Schema(implementation = SalaDTO.class))
                        )
                )
        })
    @GetMapping
    public ResponseEntity<List<SalaDTO>> getAll() {
        return ResponseEntity.ok(salaService.getAll());
    }

    @Operation(
                summary = "Devuelve una sala por su número",
                description = "Devuelve la sala que tenga el número buscado."
        )
        @ApiResponses( value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Sala buscada",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = SalaDTO.class)
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
                                                        "numero": "Debes introducir un número entero.",
                                                    }
                                                    """
                                )
                        )
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Sala no existente",
                        content = @Content(
                                mediaType = "application/json",
                                examples = @ExampleObject(
                                        name = "ObjetoNoEncontrado",
                                        summary = "Ejemplo del error devuelto si ya existe ese objeto.",
                                        value = """
                                                    {
                                                        "EntityNotFoundException": "No existe la sala 1"
                                                    }
                                                    """
                                )
                        )
                )
        })
    @GetMapping("/{numero}")
    public ResponseEntity<SalaDTO> getSalaByNumero(
            @Parameter(description = "Número de la sala a eliminar.")
            @PathVariable Integer numero) {
        return ResponseEntity.ok(salaService.getSala(numero));
    }

    @Operation(
                summary = "Crea una sala si no existe.",
                description = "Crea la sala que se le pasa en el body si no existe una con esa numeración."
        )
        @ApiResponses( value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "La sala se creo correctamente.",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = SalaDTO.class)
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
                                                        "numero": "El número no puede ser inferior a 1",
                                                        "aforo": "El aforo no puede ser inferior a 1"
                                                    }
                                                    """
                                )
                        )
                ),
                @ApiResponse(
                        responseCode = "409",
                        description = "Objeto ya existente",
                        content = @Content(
                                mediaType = "application/json",
                                examples = @ExampleObject(
                                        name = "ObjetoExistente",
                                        summary = "Ejemplo del error devuelto si ya existe una sala con el número indicado.",
                                        value = """
                                                    {
                                                        "EntityExistsException": "Ya existe una sala 1"
                                                    }
                                                    """
                                )
                        )
                )
        })
    @PostMapping
    public ResponseEntity<SalaDTO> createSala(
            @Parameter(description = "Objeto de tipo SalaDTO con los datos de la sala.")
            @RequestBody @Valid SalaDTO salaDTO
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(salaService.createSala(salaDTO));
    }

    @Operation(
                summary = "Actualiza el aforo de una sala.",
                description = "Recibe un SalaDTO con los cambios. EL número es el mismo por lo que no se actualiza su número, solo el aforo."
        )
        @ApiResponses( value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "La sala se actualizó correctamente.",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = SalaDTO.class)
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
                                                        "numero": "El número no puede ser inferior a 1",
                                                        "aforo": "El aforo no puede ser inferior a 1"
                                                    }
                                                    """
                                )
                        )
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Sala no existente",
                        content = @Content(
                                mediaType = "application/json",
                                examples = @ExampleObject(
                                        name = "SalaNoExistente",
                                        summary = "Ejemplo del error devuelto si no existe esa sala.",
                                        value = """
                                                    {
                                                        "EntityNotFoundException": "No existe la sala 1"
                                                    }
                                                    """
                                )
                        )
                )
        })
    @PutMapping
    public ResponseEntity<SalaDTO> updateSala(
            @Parameter(description = "Entidad SalaDTO con el aforo nuevo. El número no se cambia.")
            @RequestBody @Valid SalaDTO salaDTO
    ) {
        return ResponseEntity.ok(salaService.updateSala(salaDTO));
    }

    @Operation(
                summary = "Elimina la sala buscada.",
                description = "Elimina la sala si existe en base al número que se le pasa."
        )
        @ApiResponses( value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "La sala se ha leiminado correctamente.",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = SalaDTO.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Sala no existente",
                        content = @Content(
                                mediaType = "application/json",
                                examples = @ExampleObject(
                                        name = "SalaNoExistente",
                                        summary = "Ejemplo del error devuelto si no existe la sala",
                                        value = """
                                                    {
                                                        "EntityNotFoundException": "No existe la sala 1"
                                                    }
                                                    """
                                )
                        )
                )
        })
    @DeleteMapping("/{numero}")
    public ResponseEntity<SalaDTO> deleteSala(
            @Parameter(description = "Número de la sala a eliminar.")
            @PathVariable Integer numero
    ) {
        return ResponseEntity.ok(salaService.deleteSala(numero));
    }
}
