package com.tfg.API_TFG.core.controller;

import com.tfg.API_TFG.core.dto.AlergenoDTO;
import com.tfg.API_TFG.core.service.interfaces.AlergenoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
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
@RequestMapping("/alergeno")
@Tag(name = "alergenos", description = "API para la gestión de alérgenos.")
public class AlergenoController {

    private final AlergenoService alergenoService;

    @Autowired
    public AlergenoController(AlergenoService alergenoService) {
        this.alergenoService = alergenoService;
    }

    @Operation(
            summary = "Obtiene todos los alérgenos. No ordenado",
            description = "Devuelve una lista con todos los alérgenos sin ordenar."
    )
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Alérgenos encontrados",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = AlergenoDTO.class))
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<AlergenoDTO>> getAll() {
        return ResponseEntity.ok(alergenoService.getAll());
    }

    @Operation(
            summary = "Busca un alérgeno por su nombre.",
            description = "Busca si existe un alérgeno en base al nombre. No distingue mayúsuclas de minúsculas."
    )
    @ApiResponses( value = {
        @ApiResponse(
                responseCode = "200",
                description = "Alérgeno encontrado",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = AlergenoDTO.class)
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Alérgeno no encontrado",
                content = @Content
        )
    })
    @GetMapping("/{nombre}")
    public ResponseEntity<AlergenoDTO> getByNombre(
            @Parameter(description = "Nombre del alérgeno a buscar. Ignora mayus de minusculas.")
            @PathVariable String nombre) {
        return ResponseEntity.ok(alergenoService.getAlergenoByNombre(nombre));
    }

    @Operation(
            summary = "Crea un nuevo alérgeno si no existe.",
            description = "Recibe en el body un DTO y crea el alérgeno si no existe."
    )
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Alérgeno creado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AlergenoDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "El objeto ya existe",
                    content = @Content
            )
    })
    @PostMapping
    public ResponseEntity<AlergenoDTO> createAlergeno(
            @Parameter(description = "Body con un objeto que contiene el nombre del nuevo alérgeno.")
            @RequestBody @Valid AlergenoDTO alergenoDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(alergenoService.createAlergeno(alergenoDTO));
    }

    @Operation(
            summary = "Actualiza un alérgeno.",
            description = "Actualiza un alérgeno si existe."
    )
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Alérgeno actualizado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AlergenoDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Alérgeno no encontrado",
                    content = @Content
            )
    })
    @PutMapping("/{nombre}")
    public ResponseEntity<AlergenoDTO> updateAlergeno(
            @Parameter(description = "Nombre actual del parámetro.")
            @PathVariable String nombre,
            @Parameter(description = "DTO con el neuvo nombre.")
            @RequestBody @Valid AlergenoDTO alergenoDTO
    ) {
        return ResponseEntity.ok(alergenoService.updateAlergeno(nombre, alergenoDTO));
    }

    @Operation(
            summary = "Elimina un alérgeno.",
            description = "Elimina un alérgeno por su nombre. Distingue mayus de minúsculas."
    )
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Alérgeno eliminado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AlergenoDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Alérgeno no encontrado",
                    content = @Content
            )
    })
    @DeleteMapping("/{nombre}")
    public ResponseEntity<AlergenoDTO> deleteAlergeno(
            @Parameter(description = "Nombre del alérgeno a eliminar.")
            @PathVariable String nombre
    ) {
        return ResponseEntity.ok(alergenoService.deleteAlergeno(nombre));
    }
}
