package com.tfg.API_TFG.core.controller;

import com.tfg.API_TFG.core.dto.CompraDTO;
import com.tfg.API_TFG.core.service.CompraPdfService;
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
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/compra")
@Tag(name = "Compras", description = "API para las compras")
public class CompraController {
    private final CompraService compraService;
    private final CompraPdfService compraPdfService;

    @Autowired
    public CompraController(CompraService compraService, CompraPdfService compraPdfService) {
        this.compraService = compraService;
        this.compraPdfService = compraPdfService;
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
                    responseCode = "401",
                    description = "No logueado o error de login",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "NoLogueado",
                                    summary = "Ejemplo del error devuelto si el usuario no se ha logueado o ha ocurrido un error en el login.",
                                    value = """
                                                    {
                                                        "AuthenticationException": "El usuario no ha sido logueado"
                                                    }
                                                    """
                            )
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
    @GetMapping("/todas")
    public ResponseEntity<List<CompraDTO>> getAllComprasByUsuarioCorreo(
            @Parameter(description = "Token de verificación de la cuenta que contiene el correo de la cuenta.")
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(compraService.getAllByCorreoUsuario(jwt.getSubject()));
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
                    responseCode = "401",
                    description = "No logueado o error de login",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "NoLogueado",
                                    summary = "Ejemplo del error devuelto si el usuario no se ha logueado o ha ocurrido un error en el login.",
                                    value = """
                                                    {
                                                        "AuthenticationException": "El usuario no ha sido logueado"
                                                    }
                                                    """
                            )
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
    @GetMapping("/futuras")
    public ResponseEntity<List<CompraDTO>> getAllComprasFuturasByUsuarioCorreo(
            @Parameter(description = "Token de verificación de la cuenta para obtener las compras")
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(compraService.getAllByCorreoUsuarioAndAfterYesterday(jwt.getSubject()));
    }

    @Operation(
            summary = "Crea una compra junto al usuario si no existe, y las entradas.",
            description = "Crea una compra nueva y las entradas asignadas. Si no existe el usuario, lo crea.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "DTO con los datos de la compra (usuario y líneas de compra polimórficas).",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CompraDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "CompraConProducto",
                                            summary = "Compra con una línea de producto",
                                            value = """
                                                {
                                                  "correo": "ejemplo@gmail.com",
                                                  "holdToken": "6b8fb0f3-7ee8-4ef8-94ec-7bbfc9f68997",
                                                  "lineasCompra": [
                                                    {
                                                      "type": "PRODUCTO",
                                                      "numero": 1,
                                                      "nombreProducto": "Palomitas"
                                                    }
                                                  ]
                                                }
                                                """
                                    ),
                                    @ExampleObject(
                                            name = "CompraConEntrada",
                                            summary = "Compra con una línea de entrada",
                                            value = """
                                                    {
                                                         "correo": "ejemplo@gmail.com",
                                                          "holdToken": "6b8fb0f3-7ee8-4ef8-94ec-7bbfc9f68997",
                                                         "lineasCompra": [
                                                         {
                                                             "type": "ENTRADA",
                                                             "numero": 1,
                                                             "entrada": {
                                                                 "sesion": {
                                                                     "numSala": 1,
                                                                     "peliculaId": "47944cc4-527a-4639-b91e-38d95f802617",
                                                                     "horario": "2026-06-21T18:30"
                                                                 },
                                                                 "numFila": 5,
                                                                 "numButaca": 8,
                                                                 "tipo": {
                                                                    "id": 1,
                                                                    "nombre": "Adulto",
                                                                    "descripcion": "A partir de 12 años",
                                                                    "precio": 8.5
                                                                 }
                                                             }
                                                         }
                                                         ]
                                                     }
                                                """
                                    )
                            }
                    )
            )
    )
    @ApiResponses(value = {
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
                                    summary = "Ejemplo del error devuelto si alguna de las entradas ya existe (está asociada a una compra)",
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

    @Operation(
            summary = "Genera y descarga el PDF de una compra",
            description = "Genera un PDF con el resumen de la compra y un QR."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "PDF generado",
                    content = @Content(mediaType = "application/pdf")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Compra no existente",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping(value = "/{compraId}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getCompraPdf(
            @Parameter(description = "ID de la compra")
            @PathVariable UUID compraId
    ) {
        byte[] pdf = compraPdfService.generarPdf(compraId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename("compra-" + compraId + ".pdf")
                .build());
        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
}
