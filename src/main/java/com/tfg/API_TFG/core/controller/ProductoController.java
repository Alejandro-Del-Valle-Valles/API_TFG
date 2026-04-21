package com.tfg.API_TFG.core.controller;

import com.tfg.API_TFG.core.dto.ProductoDTO;
import com.tfg.API_TFG.core.service.interfaces.ProductoService;
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
@RequestMapping("/producto")
@Tag(name = "Productos", description = "API para la gestión de productos.")
public class ProductoController {

    private final ProductoService productoService;

    @Autowired
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @Operation(
                summary = "Obtiene todos los productos junto a sus alérgenos.",
                description = "Devuelve una lista con todos los productos y sus alérgenos."
        )
        @ApiResponses( value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Una lista con los productos y alérgenos",
                        content = @Content(
                                mediaType = "application/json",
                                array = @ArraySchema(schema = @Schema(implementation = ProductoDTO.class))
                        )
                )
        })
    @GetMapping
    public ResponseEntity<List<ProductoDTO>> getAll() {
        return ResponseEntity.ok(productoService.getAll());
    }

    @Operation(
                summary = "Busca y devuelve un producto por su nombre. Ignora el case.",
                description = "Busca y devuelve si existe, un producto y sus alérgenos en base al nombre."
        )
        @ApiResponses( value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Información del producto y sus alérgenos.",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ProductoDTO.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Objeto no encontrado",
                        content = @Content(
                                mediaType = "application/json",
                                examples = @ExampleObject(
                                        name = "ProductoNoEncontrado",
                                        summary = "Ejemplo del error devuelto si no se encuentra el producto buscado.",
                                        value = """
                                                {
                                                    "EntityNotFoundException": "No existe el producto con el nombre palomitas"
                                                }
                                                """
                                )
                        )
                )
        })
    @GetMapping("/{nombre}")
    public ResponseEntity<ProductoDTO> getByNombre(
            @Parameter(description = "Nombre del producto buscado. Ignora el case.")
            @PathVariable String nombre
    ) {
        return ResponseEntity.ok(productoService.getByNombre(nombre));
    }

    @Operation(
                summary = "Inserta en la BBDD un nuevo producto.",
                description = "Inserta un producto si no existe ya uno con el mismo nombre. No inserta alérgenos que no existan ya."
        )
        @ApiResponses( value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Inserta en la BBDD el producto. Si un alérgeno no existe, no se inserta.",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ProductoDTO.class)
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
                                                    "nombre": "El nombre no puede ser nulo",
                                                    "precio": "El precio no puede ser negativo
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
                                        summary = "Ejemplo del error devuelto si ya existe ese producto",
                                        value = """
                                                {
                                                    "EntityExistsException": "Ya existe un producto con el nombre palomitas"
                                                }
                                                """
                                )
                        )
                )
        })
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'EMPLEADO')")
    @PostMapping
    public ResponseEntity<ProductoDTO> createProducto(
            @Parameter(description = "Estructura en el body con los datos del nuevo producto.")
            @RequestBody @Valid ProductoDTO productoDTO
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.createProducto(productoDTO));
    }

    @Operation(
            summary = "Actualiza un producto si existe.",
            description = "Actualiza un producto que ya exista. Pone los alérgenos que se pasen en el DTO, no los añade ni los elimina, pone los que se le pasen."
    )
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Actualiza el producto con los nuevos datos.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductoDTO.class)
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
                                                    "nombre": "El nombre no puede ser nulo",
                                                    "precio": "El precio no puede ser negativo
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
                                    summary = "Ejemplo del error devuelto si ya existe ese producto con el nuevo nombre.",
                                    value = """
                                                {
                                                    "EntityExistsException": "Ya existe un producto con el nombre palomitas"
                                                }
                                                """
                            )
                    )
            )
    })
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'EMPLEADO')")
    @PutMapping("/{nombre}")
    public ResponseEntity<ProductoDTO> updateProducto(
            @Parameter(description = "Nombre del producto a actualizar.")
            @PathVariable String nombre,
            @Parameter(description = "Estructura en el body con los datos del nuevo producto.")
            @RequestBody @Valid ProductoDTO productoDTO
    ) {
        return ResponseEntity.ok(productoService.updateProducto(nombre, productoDTO));
    }

    @Operation(
                summary = "Elimina un producto.",
                description = "Elimina un producto si existe y desvincula los alérgenos que tenga asignados."
        )
        @ApiResponses( value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "El producto fue eliminado.",
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
                                                    "nombre": "El nombre no puede ser nulo"
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
                        description = "Objeto no encontrado",
                        content = @Content(
                                mediaType = "application/json",
                                examples = @ExampleObject(
                                        name = "ObjetoNoEncontrado",
                                        summary = "Ejemplo del error devuelto si no existe el producto buscado.",
                                        value = """
                                                {
                                                    "EntityNotFoundException": "No existe un producto con el nombre palomitas"
                                                }
                                                """
                                )
                        )
                )
        })
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'EMPLEADO')")
    @DeleteMapping("/{nombre}")
    public ResponseEntity<ProductoDTO> deleteProducto(
            @Parameter(description = "Nombre del producto a borrar.")
            @PathVariable String nombre
    ) {
        return ResponseEntity.ok(productoService.deleteProducto(nombre));
    }
}
