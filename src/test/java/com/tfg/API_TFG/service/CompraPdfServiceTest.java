package com.tfg.API_TFG.service;

import com.tfg.API_TFG.core.entity.Compra;
import com.tfg.API_TFG.core.entity.LineaCompra;
import com.tfg.API_TFG.core.entity.Producto;
import com.tfg.API_TFG.core.entity.Usuario;
import com.tfg.API_TFG.core.repository.CompraRepository;
import com.tfg.API_TFG.core.service.CompraPdfService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompraPdfServiceTest {

    @Mock
    CompraRepository compraRepository;

    @InjectMocks
    CompraPdfService compraPdfService;

    @Test
    void generarPdf_devuelveBytes() {
        UUID compraId = UUID.randomUUID();
        Compra compra = new Compra();
        compra.setId(compraId);

        Usuario usuario = new Usuario();
        usuario.setCorreo("cliente@correo.com");
        compra.setUsuario(usuario);

        Producto producto = new Producto();
        producto.setNombre("Palomitas");
        producto.setPrecio(new BigDecimal("3.50"));

        LineaCompra linea = new LineaCompra();
        linea.setProducto(producto);
        compra.addLineaCompra(linea);

        when(compraRepository.findById(compraId)).thenReturn(Optional.of(compra));

        byte[] pdf = compraPdfService.generarPdf(compraId);

        assertThat(pdf).isNotNull();
        assertThat(pdf.length).isGreaterThan(0);
    }
}

