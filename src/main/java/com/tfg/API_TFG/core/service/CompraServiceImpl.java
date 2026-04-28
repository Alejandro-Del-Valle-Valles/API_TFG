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
    private final BloqueoButacaRepository bloqueoButacaRepository;
    private final SesionRepository sesionRepository;
    private final ProductoRepository productoRepository;
    private final EmailService emailService;

    @Autowired
    public CompraServiceImpl(CompraRepository compraRepository, UsuarioRepository usuarioRepository,
                             EntradaRepository entradaRepository, BloqueoButacaRepository bloqueoButacaRepository,
                             SesionRepository sesionRepository,
                             ProductoRepository productoRepository, EmailService emailService) {
        this.compraRepository = compraRepository;
        this.usuarioRepository = usuarioRepository;
        this.entradaRepository = entradaRepository;
        this.bloqueoButacaRepository = bloqueoButacaRepository;
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

                SesionId sesionId = sesion.getId();
                EntradaId entradaId = new EntradaId(sesionId, eDTO.numFila(), eDTO.numButaca());

                if (entradaRepository.existsById(entradaId)) {
                    throw new EntityExistsException("Butaca ya comprada");
                }

                boolean bloqueoValido = bloqueoButacaRepository
                        .findBySesion_IdAndFilaAndButacaAndTokenAndExpiraGreaterThanEqual(
                                sesionId,
                                eDTO.numFila(),
                                eDTO.numButaca(),
                                compraDTO.holdToken(),
                                LocalDateTime.now()
                        )
                        .isPresent();
                if (!bloqueoValido) {
                    throw new EntityExistsException("Bloqueo expirado o no pertenece al token");
                }

                Entrada entrada = new Entrada();
                entradaId.setSesionId(sesion.getId());
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

        bloqueoButacaRepository.deleteByToken(compraDTO.holdToken());

        String[] info = crearInfoCorreo(guardada);

        emailService.enviarCompraHtml(compraDTO.correo(), info[0], info[1]);

        return CompraAdapter.toDTO(guardada, compraDTO.holdToken());
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

        StringBuilder html = new StringBuilder();
        html.append("<div style='font-family:Arial,sans-serif'>");
        html.append(String.format("<h2>Compra confirmada</h2><p><b>%s</b><br/>%s</p>", nombrePelicula, horario));

        html.append("<table style='border-collapse:collapse;width:100%'>");
        html.append("<thead><tr>")
                .append("<th style='border:1px solid #ddd;padding:8px;text-align:left'>Concepto</th>")
                .append("<th style='border:1px solid #ddd;padding:8px;text-align:right'>Precio</th>")
                .append("</tr></thead><tbody>");

        for (LineaCompra linea : compra.getLineaCompras()) {
            if (linea.getEntrada() != null) {
                Entrada e = linea.getEntrada();
                html.append("<tr>")
                        .append(String.format(
                                "<td style='border:1px solid #ddd;padding:8px'>Entrada - Fila %d, Butaca %d</td>",
                                e.getId().getFila(), e.getId().getButaca()))
                        .append(String.format(
                                "<td style='border:1px solid #ddd;padding:8px;text-align:right'>%.2f €</td>",
                                e.getPrecio()))
                        .append("</tr>");
            } else if (linea.getProducto() != null) {
                Producto p = linea.getProducto();
                html.append("<tr>")
                        .append(String.format(
                                "<td style='border:1px solid #ddd;padding:8px'>Producto - %s</td>",
                                escapeHtml(p.getNombre())))
                        .append(String.format(
                                "<td style='border:1px solid #ddd;padding:8px;text-align:right'>%.2f €</td>",
                                p.getPrecio()))
                        .append("</tr>");
            }
        }

        html.append("</tbody></table>");
        html.append("<p style='margin-top:16px'>Gracias por tu compra.</p>");
        html.append("</div>");

        return new String[]{asunto, html.toString()};
    }

    private String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    /**
     * Finds the product and sets it to the purchase line.
     *
     * @param linea DTO containing the product name.
     * @param lineaCompra Purchase line to set the product to.
     * @return The same purchase line with the product set.
     */
    private LineaCompra createLineaCompraProducto(LineaCompraProductoCreateDTO linea, LineaCompra lineaCompra) {
        Producto producto = productoRepository.findByNombreIgnoreCase(linea.getNombreProducto())
                .orElseThrow(() -> new EntityNotFoundException("No existe el producto con nombre " + linea.getNombreProducto()));
        lineaCompra.setProducto(producto);
        return lineaCompra;
    }

}
