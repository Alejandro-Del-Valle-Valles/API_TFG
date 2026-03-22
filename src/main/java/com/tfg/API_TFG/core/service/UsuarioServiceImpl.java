package com.tfg.API_TFG.core.service;

import com.tfg.API_TFG.adapter.UsuarioAdapter;
import com.tfg.API_TFG.core.dto.UsuarioDTO;
import com.tfg.API_TFG.core.entity.Usuario;
import com.tfg.API_TFG.core.service.interfaces.UsuarioService;
import com.tfg.API_TFG.core.repository.UsuarioRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UsuarioDTO getUsuarioByCorreo(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new EntityNotFoundException("No existe un usuario con el correo " + correo));
        return UsuarioAdapter.toDTO(usuario);
    }

    @Override
    public UsuarioDTO createUsuario(UsuarioDTO usuarioDTO) {
        Optional<Usuario> usuarioExiste = usuarioRepository.findByCorreo(usuarioDTO.correo());
        if(usuarioExiste.isPresent()) throw new EntityExistsException("Ya existe un usuario con el correo " + usuarioDTO.correo());
        Usuario usuario = new Usuario();
        usuario.setCorreo(usuarioDTO.correo());
        usuarioRepository.save(usuario);
        return UsuarioAdapter.toDTO(usuario);
    }
}
