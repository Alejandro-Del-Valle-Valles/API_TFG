package com.tfg.API_TFG.core.service;

import com.tfg.API_TFG.adapter.CompraAdapter;
import com.tfg.API_TFG.core.dto.CompraDTO;
import com.tfg.API_TFG.core.dto.LineaCompraProductoCreateDTO;
import com.tfg.API_TFG.core.entity.*;
import com.tfg.API_TFG.core.entity.id.LineaId;
import com.tfg.API_TFG.core.repository.*;
import com.tfg.API_TFG.core.service.interfaces.CompraService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompraServiceImpl implements CompraService {
    private final CompraRepository compraRepository;
    private final UsuarioRepository usuarioRepository;
    private final LineaCompraRepository lineaCompraRepository;
    private final EntradaRepository entradaRepository;
    private final ProductoRepository productoRepository;

    @Autowired
    public CompraServiceImpl(CompraRepository compraRepository, UsuarioRepository usuarioRepository,
                             LineaCompraRepository lineaCompraRepository, EntradaRepository entradaRepository,
                             ProductoRepository productoRepository) {
        this.compraRepository = compraRepository;
        this.usuarioRepository = usuarioRepository;
        this.lineaCompraRepository = lineaCompraRepository;
        this.entradaRepository = entradaRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    public List<CompraDTO> getByCorreoUsuario(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new EntityNotFoundException("No existe un usuario con correo " + correo));
        List<Compra> compras = compraRepository.findAllByUsuarioCorreo(correo);
        return compras.stream()
                .map(CompraAdapter::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public CompraDTO createCompra(CompraDTO compraDTO) {
        Usuario usuario = usuarioRepository.findByCorreo(compraDTO.correo())
                .orElseGet(() -> {
                    Usuario nuevoUsuario = new Usuario();
                    nuevoUsuario.setCorreo(compraDTO.correo());
                    return usuarioRepository.save(nuevoUsuario);
                });
        Compra compra = new Compra();
        usuario.addCompra(compra);
        compra = compraRepository.save(compra);
        List<LineaCompra> lineas = new ArrayList<>();
        Compra finalCompra = compra;
        compraDTO.lineasCompra().forEach(linea -> {
            LineaCompra lineaCompra = new LineaCompra();
            LineaId id = new LineaId(finalCompra.getId(), linea.getNumero());
            lineaCompra.setId(id);
            if(linea instanceof LineaCompraProductoCreateDTO) {
                //TODO: Modificar para que se busque el producto por su nombre.
            } else {
                Entrada entrada = new Entrada();
                //TODO: Crear entrada y guardarla para asignarla
            }
            lineas.add(lineaCompra);
        });
        compra.setLineaCompras(lineas);
        compra = compraRepository.save(compra);
        return CompraAdapter.toDTO(compra);
    }


}
