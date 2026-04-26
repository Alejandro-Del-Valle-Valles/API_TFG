package com.tfg.API_TFG.core.controller;

import com.tfg.API_TFG.core.dto.ButacasStatusResponse;
import com.tfg.API_TFG.core.dto.HoldButacaRequest;
import com.tfg.API_TFG.core.dto.HoldTokenResponse;
import com.tfg.API_TFG.core.dto.ReleaseButacaRequest;
import com.tfg.API_TFG.core.dto.SesionCrudDTO;
import com.tfg.API_TFG.core.dto.SesionCompletaDTO;
import com.tfg.API_TFG.core.service.interfaces.ButacaSyncService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sesion")
@Tag(name = "Sesiones", description = "API para las sesiones")
public class SesionController {
    private final SesionService sesionService;
    private final ButacaSyncService butacaSyncService;

    @Autowired
    public SesionController(SesionService sesionService, ButacaSyncService butacaSyncService) {
        this.sesionService = sesionService;
        this.butacaSyncService = butacaSyncService;
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
                            array = @ArraySchema(schema = @Schema(implementation = SesionCompletaDTO.class))
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<SesionCompletaDTO>> getAll() {
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
                            array = @ArraySchema(schema = @Schema(implementation = SesionCompletaDTO.class))
                    )
            )
    })
    @GetMapping("/horario")
    public ResponseEntity<List<SesionCompletaDTO>> getByRangoHorario(
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
                                schema = @Schema(implementation = SesionCompletaDTO.class)
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
    public ResponseEntity<SesionCompletaDTO> getSesion(
            @Parameter(description = "Numero de la sala de la sesión.")
            @RequestParam Integer numSala,
            @Parameter(description = "UUID de la película.")
            @RequestParam UUID peliculaId,
            @Parameter(description = "Horario de la película. Formato yyyy-MM-ddTHH:mm")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime horario) {
        return ResponseEntity.ok(sesionService.getSesion(numSala, peliculaId, horario));
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
                        responseCode = "403",
                        description = "No autorizado",
                        content = @Content(
                                mediaType = "application/json",
                                examples = @ExampleObject(
                                        name = "NoAutorizado",
                                        summary = "Ejemplo del error devuelto si el usuario no cuenta con los permisos adecuados.",
                                        value = """
                                                    {
                                                        "AccessDeniedException": "El usuario no cuenta con el rol de ADMINISTRADOR"
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
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<SesionCompletaDTO> createSesion(
            @Parameter(description = "Datos de la sesión a crear con el número de la sala, el ID de la película y el horario.")
            @RequestBody @Valid SesionCrudDTO sesionCrudDTO
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sesionService.createSesion(sesionCrudDTO));
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
                        responseCode = "403",
                        description = "No autorizado",
                        content = @Content(
                                mediaType = "application/json",
                                examples = @ExampleObject(
                                        name = "NoAutorizado",
                                        summary = "Ejemplo del error devuelto si el usuario no cuenta con los permisos adecuados.",
                                        value = """
                                                    {
                                                        "AccessDeniedException": "El usuario no cuenta con el rol de ADMINISTRADOR"
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
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping
    public ResponseEntity<SesionCompletaDTO> deleteSesion(
            @Parameter(description = "Datos de la sesión a eliminar.")
            @RequestBody @Valid SesionCrudDTO sesionCrudDTO
    ) {
        return ResponseEntity.ok(sesionService.deleteSesion(sesionCrudDTO));
    }

    @Operation(summary = "Genera un token temporal para selección de butacas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token generado",
                    content = @Content(schema = @Schema(implementation = HoldTokenResponse.class)))
    })
    @PostMapping("/{numSala}/{peliculaId}/{horario}/hold-token")
    public ResponseEntity<HoldTokenResponse> createHoldToken(
            @PathVariable Integer numSala,
            @PathVariable UUID peliculaId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime horario
    ) {
        return ResponseEntity.ok(butacaSyncService.createHoldToken(numSala, peliculaId, horario));
    }

    @Operation(summary = "Libera un hold token y sus bloqueos temporales")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Hold token liberado")
    })
    @DeleteMapping("/{numSala}/{peliculaId}/{horario}/hold-token")
    public ResponseEntity<Void> releaseHoldToken(
            @PathVariable Integer numSala,
            @PathVariable UUID peliculaId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime horario,
            @Parameter(description = "Token temporal a liberar", required = true)
            @RequestParam String token
    ) {
        butacaSyncService.releaseHoldToken(numSala, peliculaId, horario, token);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Bloquea temporalmente una butaca durante 10 minutos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Butaca bloqueada"),
            @ApiResponse(responseCode = "409", description = "Conflicto de butaca")
    })
    @PostMapping("/{numSala}/{peliculaId}/{horario}/butaca/hold")
    public ResponseEntity<Void> holdButaca(
            @PathVariable Integer numSala,
            @PathVariable UUID peliculaId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime horario,
            @Valid @RequestBody HoldButacaRequest req
    ) {
        butacaSyncService.holdSeat(numSala, peliculaId, horario, req);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Libera una butaca bloqueada por token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Butaca liberada")
    })
    @DeleteMapping("/{numSala}/{peliculaId}/{horario}/butaca/hold")
    public ResponseEntity<Void> releaseButaca(
            @PathVariable Integer numSala,
            @PathVariable UUID peliculaId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime horario,
            @Valid @RequestBody ReleaseButacaRequest req
    ) {
        butacaSyncService.releaseSeat(numSala, peliculaId, horario, req);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Consulta estado de butacas ocupadas y bloqueadas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado consultado",
                    content = @Content(schema = @Schema(implementation = ButacasStatusResponse.class)))
    })
    @GetMapping("/{numSala}/{peliculaId}/{horario}/butaca/status")
    public ResponseEntity<ButacasStatusResponse> butacaStatus(
            @PathVariable Integer numSala,
            @PathVariable UUID peliculaId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime horario
    ) {
        return ResponseEntity.ok(butacaSyncService.getStatus(numSala, peliculaId, horario));
    }
}
