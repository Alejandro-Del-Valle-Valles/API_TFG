package com.tfg.API_TFG.repository;

import com.tfg.API_TFG.core.entity.Cuenta;
import com.tfg.API_TFG.core.entity.Usuario;
import com.tfg.API_TFG.core.enums.RolUsuario;
import com.tfg.API_TFG.core.repository.CuentaRepository;
import com.tfg.API_TFG.core.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class UsuarioAndCuentaRepositoryTest {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    CuentaRepository cuentaRepository;

    @Test
    void debeCrearUsuario_yEncontrarPorCorreo() {
        String correo = "ejemplo@gmail.com";
        Usuario usuario = new Usuario();
        usuario.setCorreo(correo);
        usuarioRepository.save(usuario);

        Optional<Usuario> resultado = usuarioRepository.findByCorreo(correo);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getCorreo()).isEqualTo(correo);
    }

    @Test
    void debeCrearUsuarioYCuenta() {
        String correo = "ejemplo@gmail.com";
        Usuario usuario = new Usuario();
        usuario.setCorreo(correo);
        usuario = usuarioRepository.save(usuario);

        Cuenta cuenta = new Cuenta();
        cuenta.setContrasena("1234"); //En producción esto irá cifrado y se cifrará y descifrará en cliente
        cuenta.setUsuario(usuario);
        cuenta.setNombre("Ejemplo");
        cuenta.setRol(RolUsuario.CLIENTE);
        cuentaRepository.save(cuenta);

        Optional<Cuenta> resultado = cuentaRepository.findByUsuarioCorreo(correo);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getUsuario().getId()).isEqualTo(usuario.getId());
    }
}
