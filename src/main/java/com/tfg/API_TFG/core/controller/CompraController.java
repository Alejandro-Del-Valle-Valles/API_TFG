package com.tfg.API_TFG.core.controller;

import com.tfg.API_TFG.core.dto.CompraDTO;
import com.tfg.API_TFG.core.service.interfaces.CompraService;
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
@RequestMapping("/compra")
@Tag(name = "Compras", description = "API para las compras")
public class CompraController {
    private final CompraService compraService;

    @Autowired
    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    @Operation(
            summary = "Obtener todas las compras de un usuario",
            description = "Devuelve una lista con el histórico de compras de un usuario mediante su correo."
    )
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Compras obtenidas",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CompraDTO.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no existente",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "ObjetoNoExistente",
                                    summary = "Ejemplo del error devuelto si no existe el usuario",
                                    value = """
                                                {
                                                    "EntityNotFoundException": "No existe el usuario con correo ejemplo@gmail.com"
                                                }
                                                """
                            )
                    )
            )
    })
    @GetMapping("/todas/{correo}")
    public ResponseEntity<List<CompraDTO>> getAllComprasByUsuarioCorreo(
            @Parameter(description = "Correo del usuario del que se quieren buscar las compras")
            @PathVariable String correo
    ) {
        return ResponseEntity.ok(compraService.getAllByCorreoUsuario(correo));
    }

    @Operation(
            summary = "Obtener todas las compras de un usuario que aún tiene que usar (Presentes o futuras)",
            description = "Devuelve una lista con las compras a canjear en el presente o futuro, buscando al usuario mediante su correo."
    )
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Compras obtenidas",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CompraDTO.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no existente",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "ObjetoNoExistente",
                                    summary = "Ejemplo del error devuelto si no existe el usuario",
                                    value = """
                                                {
                                                    "EntityNotFoundException": "No existe el usuario con correo ejemplo@gmail.com"
                                                }
                                                """
                            )
                    )
            )
    })
    @GetMapping("/futuras/{correo}")
    public ResponseEntity<List<CompraDTO>> getAllComprasFuturasByUsuarioCorreo(
            @Parameter(description = "Correo del usuario del que se quieren buscar las compras")
            @PathVariable String correo
    ) {
        return ResponseEntity.ok(compraService.getAllByCorreoUsuarioAndAfterYesterday(correo));
    }

    @Operation(
                summary = "Crea una compra junto al usuario si no existe, y las entradas.",
                description = "Crea una compra nueva y las entradas asignadas. Si no existe el usuario, lo crea."
        )
        @ApiResponses( value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Compra creada exitosamente.",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = CompraDTO.class)
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
                                                        "nombre": "No existe el producto palomitas.",
                                                        "precio": "El precio de la entrada no puede ser negativo.",
                                                        "horario": "No existe una sesión con el horario indicado para la película a8f38717-7ec9-46f5-94f1-1b5cfee15f63"
                                                    }
                                                    """
                                )
                        )
                ),
                @ApiResponse(
                        responseCode = "409",
                        description = "Compra ya existente",
                        content = @Content(
                                mediaType = "application/json",
                                examples = @ExampleObject(
                                        name = "ObjetoExistente",
                                        summary = "Ejemplo del error devuelto si alguna de las entradas ya existe (Está asociada a una compra)",
                                        value = """
                                                    {
                                                        "EntityExistsException": "Ya existe la entrada que se trata de registrar."
                                                    }
                                                    """
                                )
                        )
                )
        })
    @PostMapping
    public ResponseEntity<CompraDTO> createCompra(
            @Parameter(description = "DTO con los datos de la compra (Usuario, Productos y Entradas)")
            @Valid @RequestBody CompraDTO compraDTO
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(compraService.createCompra(compraDTO));
    }
}
