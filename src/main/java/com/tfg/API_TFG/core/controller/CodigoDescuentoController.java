package com.tfg.API_TFG.core.controller;

import com.tfg.API_TFG.core.dto.CodigoDescuentoCreateDTO;
import com.tfg.API_TFG.core.dto.CodigoDescuentoDTO;
import com.tfg.API_TFG.core.service.interfaces.CodigoDescuentoService;
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
@RequestMapping("/codigo-descuento")
@Tag(
        name = "Códigos de descuento",
        description = "API para la administración de códigos de descuento."
)
public class CodigoDescuentoController {

    private final CodigoDescuentoService codigoDescuentoService;

    @Autowired
    public CodigoDescuentoController(
            CodigoDescuentoService codigoDescuentoService
    ) {
        this.codigoDescuentoService = codigoDescuentoService;
    }

    @Operation(
            summary = "Obtiene todos los códigos de descuento.",
            description = "Devuelve una lista con todos los códigos de descuento registrados."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Registros obtenidos",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(
                                            implementation = CodigoDescuentoDTO.class
                                    )
                            )
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<CodigoDescuentoDTO>> getAll() {
        return ResponseEntity.ok(codigoDescuentoService.getAll());
    }

    @Operation(
            summary = "Busca un código de descuento por ID.",
            description = "Busca y devuelve un código de descuento por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Código encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = CodigoDescuentoDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Código no existente",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "EntityNotFoundException": "No existe el código de descuento con ID: 1"
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping("/{id}")
        public ResponseEntity<CodigoDescuentoDTO> getById(
            @Parameter(description = "ID del código de descuento")
            @PathVariable Integer id
    ) {
        return ResponseEntity.ok(
                codigoDescuentoService.getById(id)
        );
    }

    @Operation(
            summary = "Busca un código de descuento por su código.",
            description = "Busca y devuelve un código de descuento a partir del texto del cupón."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Código encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = CodigoDescuentoDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Código no existente"
            )
    })
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<CodigoDescuentoDTO> getByCodigo(
            @Parameter(
                    description = "Código de descuento"
            )
            @PathVariable String codigo
    ) {
        return ResponseEntity.ok(
                codigoDescuentoService.getByCodigo(codigo)
        );
    }

    @Operation(
            summary = "Crear un código de descuento.",
            description = "Crea un nuevo código de descuento."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Código creado correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = CodigoDescuentoDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "codigo": "El código no puede estar vacío.",
                                                "porcentajeDescuento": "El porcentaje máximo es 100."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "No autorizado"
            )
    })
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<CodigoDescuentoDTO> createCodigoDescuento(
            @Parameter(description = "Datos del nuevo código de descuento")
            @RequestBody @Valid CodigoDescuentoCreateDTO dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        codigoDescuentoService.createCodigoDescuento(dto)
                );
    }

    @Operation(
            summary = "Actualizar un código de descuento.",
            description = "Actualiza un código de descuento existente."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Código actualizado correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = CodigoDescuentoDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "No autorizado"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Código no encontrado"
            )
    })
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<CodigoDescuentoDTO> updateCodigoDescuento(
            @Parameter(description = "ID del código a actualizar")
            @PathVariable Integer id,

            @Parameter(description = "Nuevos datos del código")
            @RequestBody @Valid CodigoDescuentoCreateDTO dto
    ) {
        return ResponseEntity.ok(
                codigoDescuentoService.updateCodigoDescuento(id, dto)
        );
    }
}
