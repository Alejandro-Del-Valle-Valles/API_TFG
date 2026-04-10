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
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class CompraServiceImpl implements CompraService {
    private final DateTimeFormatter FORMATO_HORARIO = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");
    private final CompraRepository compraRepository;
    private final UsuarioRepository usuarioRepository;
    private final EntradaRepository entradaRepository;
    private final SesionRepository sesionRepository;
    private final ProductoRepository productoRepository;
    private final EmailService emailService;

    @Autowired
    public CompraServiceImpl(CompraRepository compraRepository, UsuarioRepository usuarioRepository,
                             EntradaRepository entradaRepository, SesionRepository sesionRepository,
                             ProductoRepository productoRepository, EmailService emailService) {
        this.compraRepository = compraRepository;
        this.usuarioRepository = usuarioRepository;
        this.entradaRepository = entradaRepository;
        this.sesionRepository = sesionRepository;
        this.productoRepository = productoRepository;
        this.emailService = emailService;
    }

    @Override
    public List<CompraDTO> getAllByCorreoUsuario(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new EntityNotFoundException("No existe un usuario con correo " + correo));
        return usuario.getCompras().stream()
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
        Set<Integer> numLineas = new HashSet<>();

        for (LineaCompraDTO linea : compraDTO.lineasCompra()) {
            if(!numLineas.add(linea.getNumero())) throw new EntityExistsException("Existe más de una línea con el número " + linea.getNumero());
            LineaCompra lc = new LineaCompra();
            lc.setId(new LineaId(null, linea.getNumero()));

            if (linea instanceof LineaCompraProductoCreateDTO lp) {
                lc = createLineaCompraProducto(lp, lc);
            } else {
                hayEntrada = true;
                LineaCompraEntradaDTO le = (LineaCompraEntradaDTO) linea;
                EntradaDTO eDTO = le.getEntrada();

                Sesion sesion = sesionRepository.findById(
                                new SesionId(eDTO.sesion().numSala(), eDTO.sesion().peliculaId(), eDTO.sesion().horario()))
                        .orElseThrow(() -> new EntityNotFoundException("La sesión de la película no existe."));

                Entrada entrada = new Entrada();
                EntradaId entradaId = new EntradaId();
                entradaId.setFila(eDTO.numFila());
                entradaId.setButaca(eDTO.numButaca());
                entrada.setId(entradaId);

                entrada.setPrecio(eDTO.precio());
                entrada.setSesion(sesion);
                sesion.getEntradas().add(entrada);

                lc.setEntrada(entrada);
            }

            compra.addLineaCompra(lc);
        }

        if (!hayEntrada) {
            throw new IllegalArgumentException("Debe existir al menos una entrada para poder realizar la compra.");
        }

        Compra guardada = compraRepository.save(compra);

        String[] info = crearInfoCorreo(guardada);

        emailService.enviarCompra(compraDTO.correo(), info[0], info[1]);

        return CompraAdapter.toDTO(guardada);
    }


    /**
     * Crea la info que se va a enviar por correo.
     * @param compra Compra sobre la que se quiere obtener info
     * @return String[] de 2 posiciones. Posición 0 con asunto, posición 1 con cuerpo.
     */
    private String[] crearInfoCorreo(Compra compra) {
        Entrada entradaBase = compra.getLineaCompras().stream()
                .map(LineaCompra::getEntrada)
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("No se ha encontrado ninguna entrada en la compra."));

        var sesion = entradaBase.getSesion();
        String nombrePelicula = sesion.getPelicula().getNombre();
        String horario = sesion.getHorario().format(FORMATO_HORARIO);

        String asunto = String.format("Compra confirmada: %s | %s", nombrePelicula, horario);

        StringBuilder cuerpo = new StringBuilder();
        cuerpo.append(String.format("Confirmada su compra para %s | %s%n", nombrePelicula, horario));
        for (LineaCompra linea : compra.getLineaCompras()) {
            if (linea.getEntrada() != null) {
                Entrada e = linea.getEntrada();
                cuerpo.append(String.format("\tEntrada Fila %d Butaca %d Precio %.2f%n",
                        e.getId().getFila(), e.getId().getButaca(), e.getPrecio()));
            } else if (linea.getProducto() != null) {
                Producto p = linea.getProducto();
                cuerpo.append(String.format("\tProducto %s Precio %.2f%n",
                        p.getNombre(), p.getPrecio()));
            }
        }

        return new String[]{asunto, cuerpo.toString()};
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

}
