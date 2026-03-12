package com.tfg.API_TFG.exceptions;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@Tag(name = "Manejo de Errores", description = "Clase que maneja los errores producidos durante la ejecución de las respuestas.")
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ApiResponse(responseCode = "400", description = "Los atributos pasados no son correctos.")
    public ResponseEntity<String> methodArgumentNotValid(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ':' + error.getDefaultMessage())
                .collect(Collectors.joining());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ApiResponse(responseCode = "404", description = "La entidad buscada no existe o no se ha encontrado")
    public ResponseEntity<Map<String, String>> entityNotFound(EntityNotFoundException ex) {
        Map<String, String> message = new HashMap<>();
        message.put(ex.getClass().getSimpleName(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @ExceptionHandler(EntityExistsException.class)
    @ApiResponse(responseCode = "409", description = "La entidad ya existe y no se puede crear.")
    public ResponseEntity<Map<String, String>> entityNotFound(EntityExistsException ex) {
        Map<String, String> message = new HashMap<>();
        message.put(ex.getClass().getSimpleName(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
    }

    @ExceptionHandler(Exception.class)
    @ApiResponse(responseCode = "500", description = "Error no contemplado o error interno del servidor")
    public ResponseEntity<Map<String, String>> exception(Exception ex) {
        Map<String, String> message = new HashMap<>();
        message.put(ex.getClass().getSimpleName(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
    }
}
