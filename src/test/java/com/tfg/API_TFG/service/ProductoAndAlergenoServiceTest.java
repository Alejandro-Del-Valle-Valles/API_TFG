package com.tfg.API_TFG.service;

import com.tfg.API_TFG.adapter.AlergenoAdapter;
import com.tfg.API_TFG.adapter.ProductoAdapter;
import com.tfg.API_TFG.core.dto.AlergenoDTO;
import com.tfg.API_TFG.core.dto.ProductoDTO;
import com.tfg.API_TFG.core.entity.Alergeno;
import com.tfg.API_TFG.core.entity.Producto;
import com.tfg.API_TFG.core.repository.AlergenoRepository;
import com.tfg.API_TFG.core.repository.ProductoRepository;
import com.tfg.API_TFG.core.service.AlergenoServiceImpl;
import com.tfg.API_TFG.core.service.ProductoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductoAndAlergenoServiceTest {

    @Mock
    AlergenoRepository alergenoRepository;

    @Mock
    ProductoRepository productoRepository;

    @InjectMocks
    ProductoServiceImpl productoService;

    @InjectMocks
    AlergenoServiceImpl alergenoService;

    @Test
    void debeCrearProducto() {
        Producto toSave = new Producto();
        toSave.setNombre("Palomitas");
        toSave.setPrecio(new BigDecimal("7.5"));
        toSave.setStock(20);

        Producto saved = new Producto();
        saved.setId(1);
        saved.setNombre("Palomitas");
        saved.setPrecio(new BigDecimal("7.5"));
        saved.setStock(20);

        when(productoRepository.findByNombreIgnoreCase("Palomitas"))
                .thenReturn(Optional.empty());
        when(productoRepository.save(any(Producto.class)))
                .thenReturn(saved);

        ProductoDTO result = productoService.createProducto(ProductoAdapter.toDTO(toSave));

        assertThat(result.nombre()).isEqualTo("Palomitas");

        verify(productoRepository).findByNombreIgnoreCase("Palomitas");
        verify(productoRepository).save(any(Producto.class));

        verifyNoMoreInteractions(productoRepository, alergenoRepository);
    }

    @Test
    void debeCrearAlergeno() {
        Alergeno toSave = new Alergeno();
        toSave.setNombre("Gluten");

        Alergeno saved = new Alergeno();
        toSave.setId(1);
        toSave.setNombre("Gluten");

        when(alergenoRepository.findByNombreIgnoreCase("Gluten"))
                .thenReturn(Optional.empty());
        when(alergenoRepository.save(any(Alergeno.class))).thenReturn(saved);

        AlergenoDTO result = alergenoService.createAlergeno(AlergenoAdapter.toDTO(toSave));

        assertThat(result.nombre()).isEqualTo("Gluten");
        verify(alergenoRepository).findByNombreIgnoreCase("Gluten");
        verify(alergenoRepository).save(any(Alergeno.class));

        verifyNoMoreInteractions(productoRepository, alergenoRepository);
    }

    @Test
    void debeActualizarProducto() {
        Alergeno alergenoExistente = new Alergeno();
        alergenoExistente.setId(1);
        alergenoExistente.setNombre("Gluten");
        List<AlergenoDTO> alergenos = new ArrayList<>(List.of(new AlergenoDTO("Gluten")));

        Producto productoExistente = new Producto();
        productoExistente.setId(1);
        productoExistente.setNombre("Palomitas");
        productoExistente.setPrecio(new BigDecimal("7.5"));
        productoExistente.setStock(20);

        ProductoDTO cambios = new ProductoDTO(
                "Palomitas de Maíz",
                new BigDecimal("7.5"),
                15,
                alergenos
        );

        Producto guardado = new Producto();
        guardado.setId(1);
        guardado.setNombre("Palomitas de Maíz");
        guardado.setPrecio(new BigDecimal("7.5"));
        guardado.setStock(15);
        guardado.setAlergenos(new ArrayList<>(List.of(alergenoExistente)));

        when(alergenoRepository.findByNombreIgnoreCase("Gluten")).thenReturn(Optional.of(alergenoExistente));
        when(productoRepository.findByNombreIgnoreCase("Palomitas")).thenReturn(Optional.of(productoExistente));
        when(productoRepository.save(any(Producto.class))).thenReturn(guardado);

        ProductoDTO result = productoService.updateProducto("Palomitas", cambios);

        assertThat(result.nombre()).isEqualTo("Palomitas de Maíz");
        assertThat(result.stock()).isEqualTo(15);
        assertThat(result.precio()).isEqualByComparingTo("7.5");

        var captor = org.mockito.ArgumentCaptor.forClass(Producto.class);
        verify(productoRepository).findByNombreIgnoreCase("Palomitas");
        verify(productoRepository).save(captor.capture());

        Producto enviadoAGuardar = captor.getValue();
        assertThat(enviadoAGuardar.getId()).isEqualTo(1);
        assertThat(enviadoAGuardar.getNombre()).isEqualTo("Palomitas de Maíz");
        assertThat(enviadoAGuardar.getStock()).isEqualTo(15);
    }
}
