package com.tfg.API_TFG.core.service;

import com.tfg.API_TFG.adapter.CuentaAdapter;
import com.tfg.API_TFG.core.dto.CuentaDTO;
import com.tfg.API_TFG.core.dto.CuentaLoginDTO;
import com.tfg.API_TFG.core.dto.LoginDTO;
import com.tfg.API_TFG.core.entity.Cuenta;
import com.tfg.API_TFG.core.entity.Usuario;
import com.tfg.API_TFG.core.repository.UsuarioRepository;
import com.tfg.API_TFG.core.service.interfaces.CuentaService;
import com.tfg.API_TFG.core.repository.CuentaRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.Optional;

@Service
public class CuentaServiceImpl implements CuentaService {
    private final CuentaRepository cuentaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CuentaServiceImpl(CuentaRepository cuentaRepository, UsuarioRepository usuarioRepository,
                             PasswordEncoder passwordEncoder) {
        this.cuentaRepository = cuentaRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public CuentaLoginDTO login(LoginDTO login) throws AuthenticationException {
        Cuenta cuenta = cuentaRepository.findByUsuarioCorreo(login.correo())
                .orElseThrow(() -> new EntityNotFoundException("No existe una cuenta asociada al correo " + login.correo()));
        if(!passwordEncoder.matches(login.contrasena(), cuenta.getContrasena()))
            throw new AuthenticationException("La contraseña o el correo no son correctos.");
        return CuentaAdapter.toLoginDTO(cuenta);
    }

    @Override
    @Transactional
    public CuentaDTO createCuenta(CuentaDTO cuentaDTO) {
        Optional<Cuenta> cuentaExiste = cuentaRepository.findByUsuarioCorreo(cuentaDTO.correo());
        if(cuentaExiste.isPresent()) throw new EntityExistsException("Ya existe una cuenta con el correo " + cuentaDTO.correo());
        Usuario usuario = usuarioRepository.findByCorreo(cuentaDTO.correo())
                .orElseGet(() -> {
                    Usuario nuevoUsuario = new Usuario();
                    nuevoUsuario.setCorreo(cuentaDTO.correo());
                    return usuarioRepository.save(nuevoUsuario);
                });
        String hash = passwordEncoder.encode(cuentaDTO.contrasenya());
        Cuenta cuenta = new Cuenta();
        cuenta.setUsuario(usuario);
        cuenta.setNombre(cuentaDTO.nombre());
        cuenta.setContrasena(hash);
        cuenta.setRol(cuentaDTO.rol());
        usuario.setCuenta(cuenta);
        cuenta = cuentaRepository.save(cuenta);
        return CuentaAdapter.toDTO(cuenta);
    }

    @Override
    public CuentaDTO updateCuenta(CuentaDTO cuentaDTO) {
        Cuenta cuenta = cuentaRepository.findByUsuarioCorreo(cuentaDTO.correo())
                .orElseThrow(() -> new EntityNotFoundException("No existe una cuenta asociada al correo " + cuentaDTO.correo()));
        String hash = passwordEncoder.encode(cuentaDTO.contrasenya());
        cuenta.setRol(cuentaDTO.rol());
        cuenta.setContrasena(hash);
        cuenta.setNombre(cuentaDTO.nombre());
        cuentaRepository.save(cuenta);
        return CuentaAdapter.toDTO(cuenta);
    }

    @Override
    public CuentaDTO deleteCuenta(String correo) {
        Cuenta cuenta = cuentaRepository.findByUsuarioCorreo(correo)
                .orElseThrow(() -> new EntityNotFoundException("No existe una cuenta asociada al correo " + correo));
        cuentaRepository.delete(cuenta);
        return CuentaAdapter.toDTO(cuenta);
    }
}
