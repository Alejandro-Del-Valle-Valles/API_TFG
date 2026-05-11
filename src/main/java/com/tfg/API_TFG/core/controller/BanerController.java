package com.tfg.API_TFG.core.controller;

import com.tfg.API_TFG.core.dto.BanerDTO;
import com.tfg.API_TFG.core.service.interfaces.BanerService;
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
@RequestMapping("/baner")
@Tag(name = "Baner", description = "API para la gestión de baners de películas")
public class BanerController {

    private final BanerService banerService;

    @Autowired
    public BanerController(BanerService banerService) { this.banerService = banerService; }

    @Operation(
            summary = "Obtener todos los baners visibles hoy",
            description = "Devuelve una lista con todos los datos de los baners que se pueden ver hoy."
    )
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Baners obtenidos",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = BanerDTO.class))
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<BanerDTO>> getAllBanersOfToday() {
        return ResponseEntity.ok(banerService.getTodayBaners());
    }

    @Operation(
            summary = "Obtener todos los baners",
            description = "Devuelve una lista con todos los datos de los baners"
    )
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Baners obtenidos",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = BanerDTO.class))
                    )
            )
    })
    @GetMapping("/all")
    public ResponseEntity<List<BanerDTO>> getAll() {
        return ResponseEntity.ok(banerService.getAll());
    }

    @Operation(
                summary = "Crea un nuevo baner para una película",
                description = "Crea un nuevo banner si no existe uno con la misma imagen ya."
        )
        @ApiResponses( value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "El baner se creo con éxito",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = BanerDTO.class)
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
                                                        "url": "La url no puede ser nula"
                                                    }
                                                    """
                                )
                        )
                ),
                @ApiResponse(
                        responseCode = "409",
                        description = "Baner ya existente",
                        content = @Content(
                                mediaType = "application/json",
                                examples = @ExampleObject(
                                        name = "ObjetoExistente",
                                        summary = "Ejemplo del error devuelto si ya existe un baner con la misma imagen.",
                                        value = """
                                                    {
                                                        "EntityExistsException": "Ya existe un objeto con la misma imagen."
                                                    }
                                                    """
                                )
                        )
                )
        })
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<BanerDTO> createBaner(
            @Parameter(description = "Info del baner a crear")
            @RequestBody @Valid BanerDTO banerDTO
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(banerService.createBaner(banerDTO));
    }

    @Operation(
                summary = "Actualiza un baner",
                description = "Actualiza la URL y las fechas de un baner si existe, sin modificar la película asociada."
        )
        @ApiResponses( value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "El baner se actualizó correctamente",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = BanerDTO.class)
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
                                                        "url": "La url no puede ser nula"
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
                                        summary = "Ejemplo del error devuelto si no existe ese baner",
                                        value = """
                                                    {
                                                        "EntityNotFoundException": "No existe el baner con la URL antigua especificada"
                                                    }
                                                    """
                                )
                        )
                ),
                @ApiResponse(
                        responseCode = "409",
                        description = "Baner ya existente",
                        content = @Content(
                                mediaType = "application/json",
                                examples = @ExampleObject(
                                        name = "ObjetoExistente",
                                        summary = "Ejemplo del error devuelto si ya existe un baner con la misma imagen.",
                                        value = """
                                                    {
                                                        "EntityExistsException": "Ya existe un objeto con la misma imagen."
                                                    }
                                                    """
                                )
                        )
                )
        })
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<BanerDTO> updateBaner(
            @Parameter(description = "ID del baner a actualizar")
            @PathVariable int id,
            @Parameter(description = "Info nueva del baner. La película asociada no se actualiza.")
            @RequestBody @Valid BanerDTO banerDTO
    ) {
        return ResponseEntity.ok(banerService.updateBaner(id, banerDTO));
    }

    @Operation(
                summary = "Elimina el baner",
                description = "Elimina el baner por el ID dado"
        )
        @ApiResponses( value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "El baner fue eliminado con éxito.",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = BanerDTO.class)
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
                                                        "url": "La URL no puede ser nula"
                                                    }
                                                    """
                                )
                        )
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Baner no existente",
                        content = @Content(
                                mediaType = "application/json",
                                examples = @ExampleObject(
                                        name = "ObjetoNoExistente",
                                        summary = "Ejemplo del error devuelto si no existe ese baner",
                                        value = """
                                                    {
                                                        "EntityNotFoundException": "No existe el baner con la URL especificada"
                                                    }
                                                    """
                                )
                        )
                )
        })
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<BanerDTO> deleteBaner(
            @Parameter(description = "ID del baner a eliminar")
            @PathVariable int id
    ) {
        return ResponseEntity.ok(banerService.deleteBaner(id));
    }
}
