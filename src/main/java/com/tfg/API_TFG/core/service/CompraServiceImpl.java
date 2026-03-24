package com.tfg.API_TFG.core.service;

import com.tfg.API_TFG.adapter.CompraAdapter;
import com.tfg.API_TFG.core.dto.*;
import com.tfg.API_TFG.core.entity.*;
import com.tfg.API_TFG.core.entity.id.EntradaId;
import com.tfg.API_TFG.core.entity.id.LineaId;
import com.tfg.API_TFG.core.entity.id.SesionId;
import com.tfg.API_TFG.core.repository.*;
import com.tfg.API_TFG.core.service.interfaces.CompraService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CompraServiceImpl implements CompraService {
    private final CompraRepository compraRepository;
    private final UsuarioRepository usuarioRepository;
    private final LineaCompraRepository lineaCompraRepository;
    private final EntradaRepository entradaRepository;
    private final SesionRepository sesionRepository;
    private final ProductoRepository productoRepository;

    @Autowired
    public CompraServiceImpl(CompraRepository compraRepository, UsuarioRepository usuarioRepository,
                             LineaCompraRepository lineaCompraRepository, EntradaRepository entradaRepository,
                             SesionRepository sesionRepository, ProductoRepository productoRepository) {
        this.compraRepository = compraRepository;
        this.usuarioRepository = usuarioRepository;
        this.lineaCompraRepository = lineaCompraRepository;
        this.entradaRepository = entradaRepository;
        this.sesionRepository = sesionRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    public List<CompraDTO> getAllByCorreoUsuario(String correo) {
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
                LineaCompraProductoCreateDTO lineaProducto = (LineaCompraProductoCreateDTO) linea;
                lineaCompra = createLineaCompraProducto(lineaProducto, lineaCompra);
            } else {
                LineaCompraEntradaDTO lineaEntrada = (LineaCompraEntradaDTO)linea;
                lineaCompra = createLineaCompraEntrada(lineaEntrada, lineaCompra);
            }
            lineas.add(lineaCompra);
        });
        compra.setLineaCompras(lineas);
        compra = compraRepository.save(compra);
        return CompraAdapter.toDTO(compra);
    }

    /**
     * Busca el producto, y si existe, lo añade a la línea de compra.
     * @param linea DTO de la linea de compra con el nombre del producto.
     * @param lineaCompra Linea de compra a la que settear el producto
     * @return La misma línea de compra pasada como atributo pero con el producto setteado.
     */
    private LineaCompra createLineaCompraProducto(LineaCompraProductoCreateDTO linea, LineaCompra lineaCompra) {
        Producto producto = productoRepository.findByNombreIgnoreCase(linea.getNombreProducto())
                .orElseThrow(() -> new EntityNotFoundException("No existe el producto con nombre " + linea.getNombreProducto()));
        producto.addLineaCompra(lineaCompra);
        return lineaCompra;
    }

    /**
     * Comprueba si la sesión y la entrada existe. Si no existe la sesión o ya existe la entrada, lanza una excepción y noi guarda nada.
     * Crea la entrada y la asigna a la línea de compra.
     * @param linea DTO de la línea de compra
     * @param lineaCompra Linea de compra a la que añadir la entrada
     * @return LineaCompra con la entrada seteada.
     */
    private LineaCompra createLineaCompraEntrada(LineaCompraEntradaDTO linea, LineaCompra lineaCompra) {
        SesionDTO sesionDTO = linea.getEntrada().sesion();
        SesionId sesionId = new SesionId(sesionDTO.numSala(), sesionDTO.peliculaId(), sesionDTO.horario());
        Sesion sesion = sesionRepository.findById(sesionId)
                .orElseThrow(() -> new EntityNotFoundException("La sesión de la película no existe."));

        EntradaDTO entradaDTO = linea.getEntrada();
        EntradaId entradaId = new EntradaId(entradaDTO.sesion().numSala(), entradaDTO.sesion().peliculaId(),
                entradaDTO.sesion().horario(), entradaDTO.numFila(), entradaDTO.numButaca());
        Optional<Entrada> entradaExiste = entradaRepository.findById(entradaId);
        if(entradaExiste.isPresent()) throw new EntityExistsException("Ya existe la entrada que se trata de registrar");

        Entrada entrada = new Entrada();
        entrada.setId(entradaId);
        entrada.setPrecio(entradaDTO.precio());
        sesion.addEntrada(entrada);

        entrada = entradaRepository.save(entrada);

        lineaCompra.setEntrada(entrada);
        return lineaCompra;
    }

}
