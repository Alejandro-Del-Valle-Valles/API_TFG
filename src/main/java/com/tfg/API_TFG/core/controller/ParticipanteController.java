package com.tfg.API_TFG.core.controller;

import com.tfg.API_TFG.core.dto.ParticipanteDTO;
import com.tfg.API_TFG.core.service.interfaces.ParticipanteService;
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
@RequestMapping("/participante")
@Tag(name = "Participantes", description = "API para los participantes")
public class ParticipanteController {
    private final ParticipanteService participanteService;

    @Autowired
    public ParticipanteController(ParticipanteService participanteService) {
        this.participanteService = participanteService;
    }

    @Operation(
                summary = "Obtener todos los participantes.",
                description = "Devuelve una lista con todos los datos de los participantes."
        )
        @ApiResponses( value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Participantes obtenidos",
                        content = @Content(
                                mediaType = "application/json",
                                array = @ArraySchema(schema = @Schema(implementation = ParticipanteDTO.class))
                        )
                )
        })
    @GetMapping
    public ResponseEntity<List<ParticipanteDTO>> getAll() {
        return ResponseEntity.ok(participanteService.getAll());
    }

    @Operation(
            summary = "Obtener todos los participantes que contenga la plabara buscada.",
            description = "Devuelve una lista con todos los datos de los participantes que contengan en el nombre la palabra buscada independendiente del case"
    )
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Participantes obtenidos",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ParticipanteDTO.class))
                    )
            )
    })
    @GetMapping("/{nombre}")
    public ResponseEntity<List<ParticipanteDTO>> getAllByNombre(
            @Parameter(description = "Nombre de los participantes a buscar. Encuentra todos los qe contenga la palabra buscada.")
            @PathVariable String nombre) {
        return ResponseEntity.ok(participanteService.getByNombre(nombre));
    }

    @Operation(
                summary = "Obtiene un participante por su ID",
                description = "Obtiene y devuelve un participante por el ID buscado."
        )
        @ApiResponses( value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Participante encontrado",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ParticipanteDTO.class)
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
                                                        "id": "El id debe ser mayor que 1",
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
                                        summary = "Ejemplo del error devuelto si ya existe ese participante",
                                        value = """
                                                    {
                                                        "EntityExistsException": "No existe el participante con ID 1"
                                                    }
                                                    """
                                )
                        )
                )
        })
    @GetMapping("/{id}")
    public ResponseEntity<ParticipanteDTO> getById(
            @Parameter(description = "ID del participante buscado.")
            @PathVariable Integer id) {
        return ResponseEntity.ok(participanteService.getById(id));
    }

    @Operation(
                summary = "Crea un nuevo participante",
                description = "Crea un nuevo participante con el nombre dado."
        )
        @ApiResponses( value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "El participante se creo con éxito.",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ParticipanteDTO.class)
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
                                                        "atributo": "Problema",
                                                        "atributo": "Problema"
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
                                        summary = "Ejemplo del error devuelto si ya existe ese objeto.",
                                        value = """
                                                    {
                                                        "EntityExistsException": "Ya existe un objeto con el nombre"
                                                    }
                                                    """
                                )
                        )
                )
        })
    @PostMapping("/{nombre}")
    public ResponseEntity<ParticipanteDTO> createParticipante(
            @Parameter(description = "Nombre del nuevo participante a crear.")
            @PathVariable String nombre) {
        return ResponseEntity.status(HttpStatus.CREATED).body(participanteService.createParticipante(nombre));
    }

    @Operation(
                summary = "Actualiza un participante",
                description = "Actualiza el nombre del participante."
        )
        @ApiResponses( value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "El participante fue actualizado con éxito.",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ParticipanteDTO.class)
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
                                                        "id": "El id no puede ser inferior a 1.",
                                                        "nombre": "El nombre no puede contener más de 50 caracteres."
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
                                        summary = "Ejemplo del error devuelto si ya existe ese participante",
                                        value = """
                                                    {
                                                        "EntityExistsException": "No existe el participante con ID 1"
                                                    }
                                                    """
                                )
                        )
                )
        })
    @PutMapping
    public ResponseEntity<ParticipanteDTO> updateParticipante(
            @Parameter(description = "ParticipanteDTO con el nuevo nombre.")
            @RequestBody @Valid ParticipanteDTO participanteDTO
    ) {
        return ResponseEntity.ok(participanteService.updateParticipante(participanteDTO));
    }

    @Operation(
                summary = "Elimina un participante",
                description = "Elimina un participante por el ID dado."
        )
        @ApiResponses( value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "El participante fue eliminado con éxito.",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ParticipanteDTO.class)
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
                                                        "id": "El ID no puede ser menor que 1."
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
                                        summary = "Ejemplo del error devuelto si no existe ese participante",
                                        value = """
                                                    {
                                                        "EntityNotFoundException": "No existe el participante con ID 1"
                                                    }
                                                    """
                                )
                        )
                )
        })
    @DeleteMapping("/{id}")
    public ResponseEntity<ParticipanteDTO> deleteParticipante(
            @Parameter(description = "ID del participante a eliminar.")
            @PathVariable Integer id
    ) {
        return ResponseEntity.ok(participanteService.deleteParticipante(id));
    }
}
