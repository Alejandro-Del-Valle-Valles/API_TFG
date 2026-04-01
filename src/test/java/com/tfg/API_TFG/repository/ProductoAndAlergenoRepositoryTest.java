package com.tfg.API_TFG.repository;

import com.tfg.API_TFG.core.entity.Alergeno;
import com.tfg.API_TFG.core.entity.Producto;
import com.tfg.API_TFG.core.repository.AlergenoRepository;
import com.tfg.API_TFG.core.repository.ProductoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests de repositorio (JPA) para productos y alérgenos
 */
@DataJpaTest
class ProductoAndAlergenoRepositoryTest {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private AlergenoRepository alergenoRepository;

    @Test
    void debeGuardarAlergeno_yEncontrarloPorNombreIgnoreCase() {
        Alergeno alergeno = new Alergeno();
        alergeno.setNombre("Gluten");
        alergenoRepository.save(alergeno);

        Optional<Alergeno> resultado = alergenoRepository.findByNombreIgnoreCase("gluten");

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNombre()).isEqualTo("Gluten");
    }

    @Test
    void debeGuardarProducto_yEncontrarloPorNombre() {
        Alergeno alergeno = new Alergeno();
        alergeno.setNombre("Gluten");
        alergeno = alergenoRepository.save(alergeno);
        List<Alergeno> alergenos = new ArrayList<>(List.of(alergeno));

        Producto producto = new Producto();
        producto.setNombre("Palomitas");
        producto.setPrecio(new BigDecimal("7.5"));
        producto.setStock(20);
        producto.setAlergenos(alergenos);
        productoRepository.save(producto);

        Optional<Producto> resultado = productoRepository.findByNombreIgnoreCase("palomitas");

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNombre()).isEqualTo("Palomitas");
        assertThat(resultado.get().getPrecio()).isEqualTo(new BigDecimal("7.5"));
        assertThat(resultado.get().getStock()).isEqualTo(20);
        assertThat(resultado.get().getAlergenos())
                .extracting(Alergeno::getId)
                .containsExactly(alergeno.getId());
    }

    @Test
    void debeGuardarProducto_yActualizarlo() {
        Producto producto = new Producto();
        producto.setNombre("Palomitas");
        producto.setPrecio(new BigDecimal("7.5"));
        producto.setStock(20);
        productoRepository.save(producto);

        Producto productoAntiguo = productoRepository.findByNombreIgnoreCase("palomitas")
                .orElseThrow(() -> new EntityNotFoundException("No existe el producto palomitas"));

        Alergeno alergeno = new Alergeno();
        alergeno.setNombre("Gluten");
        alergeno = alergenoRepository.save(alergeno);
        List<Alergeno> alergenos = new ArrayList<>(List.of(alergeno));

        productoAntiguo.setNombre("Palomitas de Maíz");
        productoAntiguo.setStock(15);
        productoAntiguo.setAlergenos(alergenos);
        productoRepository.save(productoAntiguo);

        Optional<Producto> resultado = productoRepository.findByNombreIgnoreCase("Palomitas de Maíz");

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(productoAntiguo.getId());
        assertThat(resultado.get().getNombre()).isEqualTo("Palomitas de Maíz");
        assertThat(resultado.get().getPrecio()).isEqualTo(new BigDecimal("7.5"));
        assertThat(resultado.get().getStock()).isEqualTo(15);
        assertThat(resultado.get().getAlergenos())
                .extracting(Alergeno::getId)
                .containsExactly(alergeno.getId());
    }

    @Test
    void debeEliminarAlergeno() {
        Alergeno alergeno = new Alergeno();
        alergeno.setNombre("Gluten");
        alergenoRepository.save(alergeno);

        Alergeno alergenoEliminar = alergenoRepository.findByNombreIgnoreCase("gluten")
                .orElseThrow(() -> new EntityNotFoundException("No existe el alérgeno gluten"));
        alergenoRepository.delete(alergenoEliminar);

        Optional<Alergeno> resultado = alergenoRepository.findByNombreIgnoreCase("gluten");

        assertThat(resultado).isNotPresent();
    }

    @Test
    void debeEliminarProducto() {
        Producto producto = new Producto();
        producto.setNombre("Palomitas");
        producto.setPrecio(new BigDecimal("7.5"));
        producto.setStock(20);
        productoRepository.save(producto);

        Producto productoEliminar = productoRepository.findByNombreIgnoreCase("palomitas")
                .orElseThrow(() -> new EntityNotFoundException("No existe el producto palomitas."));
        productoRepository.delete(productoEliminar);

        Optional<Producto> resultado = productoRepository.findByNombreIgnoreCase("palomitas");

        assertThat(resultado).isNotPresent();
    }
}