package com.tfg.API_TFG.core.controller;

import com.tfg.API_TFG.core.dto.CuentaDTO;
import com.tfg.API_TFG.core.dto.CuentaLoginDTO;
import com.tfg.API_TFG.core.dto.CuentaUpdateDTO;
import com.tfg.API_TFG.core.dto.LoginDTO;
import com.tfg.API_TFG.core.service.interfaces.CuentaService;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("/cuenta")
@Tag(name = "Cuenta", description = "API para las cuentas")
public class CuentaController {
    private final CuentaService cuentaService;

    @Autowired
    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @Operation(
                summary = "Logea al usuario",
                description = "Logea y al usuario y le devuelve la info de su cuenta"
        )
        @ApiResponses( value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Se inició sesión",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = CuentaLoginDTO.class)
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
                                                        "correo": "El correo no puede ser nulo"
                                                    }
                                                    """
                                )
                        )
                ),
                @ApiResponse(
                        responseCode = "401",
                        description = "Cuenta no existente",
                        content = @Content(
                                mediaType = "application/json",
                                examples = @ExampleObject(
                                        name = "ObjetoNoExistente",
                                        summary = "Ejemplo del error devuelto si no existe esa cuenta",
                                        value = """
                                                    {
                                                        "AuthenticationException": "La contraseña o el correo no son correctas."
                                                    }
                                                    """
                                )
                        )
                )
        })
    @PostMapping("/login")
    public ResponseEntity<CuentaLoginDTO> login(
            @Parameter(description = "Correo y contraseña en plano dela cuenta.")
            @RequestBody LoginDTO login
            ) throws AuthenticationException {
        return ResponseEntity.ok(cuentaService.login(login));
    }

    @Operation(
                summary = "Crea una cuenta y la asigna al usuario con el correo o crea el usuario si no existe.",
                description = "Crea una cuenta y la asigna al usuario mediante el correo. Si no existe el usuario, lo crea."
        )
        @ApiResponses( value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Cuenta creada correctamente",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = CuentaDTO.class)
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
                                                        "correo": "El correo no debe contener más de 100 caracteres",
                                                        "nombre": "El nombre no puede ser nulo."
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
                                                        "EntityExistsException": "Ya existe una cuenta asociada al correo ejemplo@gmail.com"
                                                    }
                                                    """
                                )
                        )
                )
        })
    @PostMapping("/registro")
    public ResponseEntity<CuentaDTO> createCuenta(
            @Parameter(description = "Datos de la nueva cuenta.")
            @RequestBody @Valid CuentaDTO cuentaDTO
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cuentaService.createCuenta(cuentaDTO));
    }

    @Operation(
                summary = "Actualiza una cuenta si existe. No se puede actualizar el correo.",
                description = "Actualiza los datos de una cuenta si existe. El correo no se puede actualizar."
        )
        @ApiResponses( value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "La cuenta fue actualizada con éxito",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = CuentaDTO.class)
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
                                                        "correo": "El correo no debe contener más de 100 caracteres",
                                                        "nombre": "El nombre no puede ser nulo."
                                                    }
                                                    """
                                )
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
                        description = "Objeto no existente",
                        content = @Content(
                                mediaType = "application/json",
                                examples = @ExampleObject(
                                        name = "ObjetoNoExistente",
                                        summary = "Ejemplo del error devuelto si no existe esa cuenta",
                                        value = """
                                                    {
                                                        "EntityNotFoundException": "No existe una cuenta con el correo ejemplo@gmail.com"
                                                    }
                                                    """
                                )
                        )
                )
        })
    @PutMapping("/me")
    public ResponseEntity<CuentaDTO> updateCuenta(
            @Parameter(description = "Token de verificación de la cuenta.")
            @AuthenticationPrincipal Jwt jwt,
            @Parameter(description = "Datos nuevos de la cuenta.")
            @RequestBody @Valid CuentaUpdateDTO cuentaUpdate
    ) throws AuthenticationException {
        return ResponseEntity.ok(cuentaService.updateCuenta(jwt.getSubject(), cuentaUpdate));
    }

    @Operation(
                summary = "Elimina la cuenta",
                description = "Elimina la cuenta en base al correo pasado. Primero comprueba que las credenciales son correctas."
        )
        @ApiResponses( value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "La cuenta fue eliminado con éxito.",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = CuentaDTO.class)
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
                                                        "correo": "El correo no debe ser nulo"
                                                    }
                                                    """
                                )
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
                        description = "Cuenta no existente",
                        content = @Content(
                                mediaType = "application/json",
                                examples = @ExampleObject(
                                        name = "ObjetoNoExistente",
                                        summary = "Ejemplo del error devuelto si no existe la cuenta",
                                        value = """
                                                    {
                                                        "EntityNotFoundException": "No existe una cuenta con el correo ejemplo@gmail.com"
                                                    }
                                                    """
                                )
                        )
                )
        })
    @DeleteMapping("/me")
    public ResponseEntity<CuentaDTO> deleteCuenta(
            @Parameter(description = "Correo y contraseña en plano de la cuenta a eliminar")
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(cuentaService.deleteCuenta(jwt.getSubject()));
    }
}
