package com.tfg.API_TFG.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfg.API_TFG.core.dto.CuentaUpdateDTO;
import com.tfg.API_TFG.core.entity.Cuenta;
import com.tfg.API_TFG.core.entity.Usuario;
import com.tfg.API_TFG.core.enums.RolUsuario;
import com.tfg.API_TFG.core.repository.CuentaRepository;
import com.tfg.API_TFG.core.repository.UsuarioRepository;
import com.tfg.API_TFG.core.service.JwtTokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CuentaAndUsuarioControllerTest {

    @Autowired MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired UsuarioRepository usuarioRepository;
    @Autowired CuentaRepository cuentaRepository;

    @Autowired JwtTokenService jwtTokenService;
    @Autowired PasswordEncoder passwordEncoder;

    @Test
    void updateMe_conTokenValido_actualizaNombreYContrasena() throws Exception {
        String correo = "user@test.com";

        Usuario usuario = new Usuario();
        usuario.setCorreo(correo);
        usuario = usuarioRepository.save(usuario);

        Cuenta cuenta = new Cuenta();
        cuenta.setNombre("Nombre Inicial");
        cuenta.setContrasena(passwordEncoder.encode("PasswordInicial123"));
        cuenta.setRol(RolUsuario.CLIENTE);
        cuenta.setUsuario(usuario);

        usuario.setCuenta(cuenta);
        usuarioRepository.save(usuario);

        String token = jwtTokenService.createToken(correo, RolUsuario.CLIENTE);

        CuentaUpdateDTO cambios = new CuentaUpdateDTO(
                "Nombre Actualizado",
                "NuevaPassword123",
                RolUsuario.CLIENTE
        );

        mockMvc.perform(put("/cuenta/me")
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(cambios)))
                .andExpect(status().isOk());

        Optional<Cuenta> actualizadaOpt = cuentaRepository.findByUsuarioCorreo(correo);
        assertThat(actualizadaOpt).isPresent();

        Cuenta actualizada = actualizadaOpt.get();
        assertThat(actualizada.getNombre()).isEqualTo("Nombre Actualizado");
        assertThat(passwordEncoder.matches("NuevaPassword123", actualizada.getContrasena())).isTrue();
    }

    @Test
    void updateMe_sinToken_devuelve401() throws Exception {
        CuentaUpdateDTO cambios = new CuentaUpdateDTO("Nombre", "Password123", RolUsuario.CLIENTE);
        mockMvc.perform(put("/cuenta/me")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cambios)))
                .andExpect(status().isUnauthorized());
    }
}
