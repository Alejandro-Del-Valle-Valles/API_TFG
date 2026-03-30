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

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    public List<CompraDTO> getAllByCorreoUsuarioAndAfterYesterday(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new EntityNotFoundException("No existe un usuario con correo " + correo));
        List<Compra> compras = compraRepository.findDistinctByUsuarioCorreoAndLineaComprasEntradaSesionHorarioAfter(correo, LocalDateTime.now().minusDays(1));
        return compras.stream()
                .map(CompraAdapter::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public CompraDTO createCompra(CompraDTO compraDTO) {
        Usuario usuario = usuarioRepository.findByCorreo(compraDTO.correo())
                .orElseGet(() -> {
                    Usuario u = new Usuario();
                    u.setCorreo(compraDTO.correo());
                    return usuarioRepository.save(u);
                });

        Compra compra = new Compra();
        compra.setUsuario(usuario);

        boolean hayEntrada = false;
        Set<EntradaId> idsEnEstaCompra = new HashSet<>();

        for (LineaCompraDTO linea : compraDTO.lineasCompra()) {
            LineaCompra lc = new LineaCompra();
            lc.setId(new LineaId(null, linea.getNumero()));

            if (linea instanceof LineaCompraProductoCreateDTO lp) {
                lc = createLineaCompraProducto(lp, lc);
            } else {
                hayEntrada = true;
                LineaCompraEntradaDTO le = (LineaCompraEntradaDTO) linea;
                EntradaDTO eDTO = le.getEntrada();

                SesionId sesionId = new SesionId(eDTO.sesion().numSala(),  eDTO.sesion().peliculaId(),  eDTO.sesion().horario());

                EntradaId entradaId = new EntradaId( sesionId,  eDTO.numFila(),  eDTO.numButaca() );
                if (!idsEnEstaCompra.add(entradaId))
                    throw new IllegalArgumentException("La entrada " + entradaId + " está duplicada en la misma compra.");
                if (entradaRepository.existsById(entradaId))
                    throw new EntityExistsException("Ya existe la entrada " + entradaId);

                Sesion sesion = sesionRepository.findById(
                                new SesionId(eDTO.sesion().numSala(), eDTO.sesion().peliculaId(), eDTO.sesion().horario()))
                        .orElseThrow(() -> new EntityNotFoundException("La sesión de la película no existe."));

                Entrada entrada = new Entrada();
                entrada.setId(entradaId);
                entrada.setPrecio(eDTO.precio());
                sesion.addEntrada(entrada);

                lc.setEntrada(entrada);
            }

            compra.addLineaCompra(lc);
        }

        if (!hayEntrada) {
            throw new IllegalArgumentException("Debe existir al menos una entrada para poder realizar la compra.");
        }

        Compra guardada = compraRepository.save(compra);
        return CompraAdapter.toDTO(guardada);
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
        SesionDTO s = linea.getEntrada().sesion();
        SesionId sesionId = new SesionId(s.numSala(), s.peliculaId(), s.horario());
        Sesion sesion = sesionRepository.findById(sesionId)
                .orElseThrow(() -> new EntityNotFoundException("La sesión de la película no existe."));

        EntradaDTO e = linea.getEntrada();
        EntradaId entradaId = new EntradaId(
                sesionId,
                e.numFila(),
                e.numButaca()
        );
        if (entradaRepository.existsById(entradaId))
            throw new EntityExistsException("Ya existe la entrada que se trata de registrar");

        Entrada entrada = new Entrada();
        entrada.setId(entradaId);
        entrada.setPrecio(e.precio());
        sesion.addEntrada(entrada);
        lineaCompra.setEntrada(entrada);
        entrada.setLineaCompra(lineaCompra);

        return lineaCompra;
    }

}
