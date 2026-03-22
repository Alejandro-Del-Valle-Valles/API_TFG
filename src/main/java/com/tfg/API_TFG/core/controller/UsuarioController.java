package com.tfg.API_TFG.core.controller;

import com.tfg.API_TFG.core.dto.UsuarioDTO;
import com.tfg.API_TFG.core.service.interfaces.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

@RestController
@RequestMapping("/usuario")
@Tag(name = "Usuarios", description = "API para los usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(
                summary = "Busca a un usuario por su correo.",
                description = "Busca y devuelve un usuario por el correo buscado"
        )
        @ApiResponses( value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "El usuario se encontró",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = UsuarioDTO.class)
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
                                                        "correo": "El correo no puede ser nulo."
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
                                        summary = "Ejemplo del error devuelto si no existe ese usuario.",
                                        value = """
                                                    {
                                                        "EntityNotFoundException": "No existe el usuario con correo ejemplo@gmail.com"
                                                    }
                                                    """
                                )
                        )
                )
        })
    @GetMapping("/{correo}")
    public ResponseEntity<UsuarioDTO> getUsuarioByCorreo(
            @Parameter(description = "Correo del usuario buscado")
            @PathVariable String correo
    ) {
        return ResponseEntity.ok(usuarioService.getUsuarioByCorreo(correo));
    }

    @Operation(
                summary = "Crea un nuevo usuario",
                description = "Crea un nuevo usuario con el correo pasado si no existe uno con ese correo ya asignado."
        )
        @ApiResponses( value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "El usuario se creo correctamente",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = UsuarioDTO.class)
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
                                                        "correo": "Ya existe un usuario con el correo ejemplo@gmail.com"
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
                                        summary = "Ejemplo del error devuelto si ya existe un usuario con ese correo.",
                                        value = """
                                                    {
                                                        "EntityExistsException": "Ya existe un usuario con el correo ejemplo@gmail.com"
                                                    }
                                                    """
                                )
                        )
                )
        })
    @PostMapping
    public ResponseEntity<UsuarioDTO> createUsuario(
            @Parameter(description = "UsuarioDTO con el correo del nuevo usuario.")
            @RequestBody @Valid UsuarioDTO usuarioDTO
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.createUsuario(usuarioDTO));
    }
}
