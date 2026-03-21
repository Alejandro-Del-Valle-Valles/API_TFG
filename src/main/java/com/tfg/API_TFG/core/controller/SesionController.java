package com.tfg.API_TFG.core.controller;

import com.tfg.API_TFG.core.dto.PeliculaCreateDTO;
import com.tfg.API_TFG.core.dto.PeliculaDTO;
import com.tfg.API_TFG.core.dto.SesionCrudDTO;
import com.tfg.API_TFG.core.dto.SesionDTO;
import com.tfg.API_TFG.core.service.interfaces.SesionService;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/sesion")
@Tag(name = "Sesiones", description = "API para las sesiones")
public class SesionController {
    private final SesionService sesionService;

    @Autowired
    public SesionController(SesionService sesionService) {
        this.sesionService = sesionService;
    }

    @Operation(
            summary = "Obtener todos las sesiones",
            description = "Devuelve una lista con todos los datos de las sesiones"
    )
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Registros obtenidos",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = SesionDTO.class))
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<SesionDTO>> getAll() {
        return ResponseEntity.ok(sesionService.getAll());
    }

    @Operation(
            summary = "Obtener todas las sesiones que estén entre 2 fechas.",
            description = "Devuelve una lista con todos los datos de las sesiones."
    )
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Registros obtenidos",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = SesionDTO.class))
                    )
            )
    })
    @GetMapping("/horario")
    public ResponseEntity<List<SesionDTO>> getByRangoHorario(
            @Parameter(description = "Fecha y hora de inicio", example = "2026-06-21T18:00:00", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,

            @Parameter(description = "Fecha y hora de fin", example = "2026-06-21T23:59:59", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin
    ) {
        return ResponseEntity.ok(sesionService.getAllBetweenHorarios(inicio, fin));
    }

    @Operation(
                summary = "Obtiene una sesión en base a los datos pasados",
                description = "Busca y devuelve la sesión buscada si existe."
        )
        @ApiResponses( value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "La sesión fue encontrada",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = Object.class)
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
                                                        "numSala": "El número de la sala debe ser positivo.",
                                                        "horario": "El horario de la película no puede ser nulo."
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
                                        summary = "Ejemplo del error devuelto si no existe esa sesion.",
                                        value = """
                                                    {
                                                        "EntityNotFoundException": "No existe la sesión para la sala 1 para la eplícula 62a2782f-eb5f-47ba-9ee5-7f2e6933fcde con horario 2026-06-11T16:00"
                                                    }
                                                    """
                                )
                        )
                )
        })
    @GetMapping("/sesion")
    public ResponseEntity<SesionDTO> getSesion(
            @Parameter(description = "Datos de la sesión buscada.")
            @RequestBody @Valid SesionCrudDTO sesionCrudDTO
    ) {
        return ResponseEntity.ok(sesionService.getSesion(sesionCrudDTO));
    }

    @Operation(
                summary = "Crea una sesión",
                description = "Crea una nueva sesión si no existe. No puede coincidir en sala, película y horario con otra sesión."
        )
        @ApiResponses( value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "La sesión fue creada con éxito",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = SesionCrudDTO.class)
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
                                                        "numSala": "El número de la sala debe ser positivo.",
                                                        "horario": "El horario no puede ser nulo."
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
                                        summary = "Ejemplo del error devuelto si ya existe esa sesión.",
                                        value = """
                                                    {
                                                        "EntityExistsException": "Ya existe una sesión para la sala 1 con la película 62a2782f-eb5f-47ba-9ee5-7f2e6933fcde en el horario 2026-06-11T16:00"
                                                    }
                                                    """
                                )
                        )
                )
        })
    @PostMapping
    public ResponseEntity<SesionDTO> createSesion(
            @Parameter(description = "Datos de la sesión a crear con el número de la sala, el ID de la película y el horario.")
            @RequestBody @Valid SesionCrudDTO sesionCrudDTO
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sesionService.createSesion(sesionCrudDTO));
    }

    @Operation(
                summary = "Actualiza los datos de una sesión.",
                description = "Actualiza los dato de una sesión si existe y si no existe una sesión con los nuevos datos."
        )
        @ApiResponses( value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "La sesión se actualizó con éxito.",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = Object.class)
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
                                                        "numSala": El número de la sala debe ser positivo.",
                                                        "horario": "El horario no debe ser nulo."
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
                                        summary = "Ejemplo del error devuelto si no existe la sesión",
                                        value = """
                                                    {
                                                        "EntityNotFoundException": "No existe la función para la sala 1 con la película 62a2782f-eb5f-47ba-9ee5-7f2e6933fcde en el horario 2026-06-11T16:00"
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
                                        summary = "Ejemplo del error devuelto si ya existe esa sesión.",
                                        value = """
                                                    {
                                                        "EntityExistsException": "Ya existe una sesión para la sala 1 con la película 62a2782f-eb5f-47ba-9ee5-7f2e6933fcde en el horario 2026-06-11T16:00"
                                                    }
                                                    """
                                )
                        )
                )
        })
    @PutMapping
    public ResponseEntity<SesionDTO> updateSesion(
            @Parameter(description = "Datos de la sesión actual")
            @RequestBody @Valid SesionCrudDTO actual,
            @Parameter(description = "Datos nuevos de la sesión")
            @RequestBody @Valid SesionCrudDTO nuevo
    ) {
        return ResponseEntity.ok(sesionService.updateSesion(actual, nuevo));
    }

    @Operation(
                summary = "Elimina la sesión",
                description = "Elimina la sesión si existe"
        )
        @ApiResponses( value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "La sesión fue eliminado con éxito.",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = SesionCrudDTO.class)
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
                                                        "numSala": "El numero de la sala debe ser positivo",
                                                        "horario": "El horario no puede ser nulo."
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
                                        summary = "Ejemplo del error devuelto si no existe la sesión",
                                        value = """
                                                    {
                                                        "EntityNotFoundException": "No existe la sesión para la sala 1 con la película af58928b-7544-457b-9fc1-79eff8046b1f en el horario 2026-06-11T16:00"
                                                    }
                                                    """
                                )
                        )
                )
        })
    @DeleteMapping
    public ResponseEntity<SesionDTO> deleteSesion(
            @Parameter(description = "Datos de la sesión a eliminar.")
            @RequestBody @Valid SesionCrudDTO sesionCrudDTO
    ) {
        return ResponseEntity.ok(sesionService.deleteSesion(sesionCrudDTO));
    }
}
