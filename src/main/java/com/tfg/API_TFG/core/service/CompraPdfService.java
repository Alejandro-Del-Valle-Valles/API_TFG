package com.tfg.API_TFG.core.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.tfg.API_TFG.core.entity.Compra;
import com.tfg.API_TFG.core.entity.Entrada;
import com.tfg.API_TFG.core.entity.LineaCompra;
import com.tfg.API_TFG.core.entity.Producto;
import com.tfg.API_TFG.core.repository.CompraRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class CompraPdfService {
    private static final DateTimeFormatter FORMATO_HORARIO = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");
    private final CompraRepository compraRepository;

    public CompraPdfService(CompraRepository compraRepository) {
        this.compraRepository = compraRepository;
    }

    @Transactional
    public byte[] generarPdf(UUID compraId) {
        Compra compra = compraRepository.findById(compraId)
                .orElseThrow(() -> new EntityNotFoundException("No existe la compra con ID: " + compraId));
        return generarPdf(compra);
    }

    private byte[] generarPdf(Compra compra) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("Resumen de compra"));
            document.add(new Paragraph("Compra: " + compra.getId()));
            document.add(new Paragraph("Correo: " + compra.getUsuario().getCorreo()));

            Entrada entradaBase = compra.getLineaCompras().stream()
                    .map(LineaCompra::getEntrada)
                    .filter(e -> e != null)
                    .findFirst()
                    .orElse(null);
            if (entradaBase != null) {
                String horario = entradaBase.getSesion().getHorario().format(FORMATO_HORARIO);
                String pelicula = entradaBase.getSesion().getPelicula().getNombre();
                document.add(new Paragraph("Pelicula: " + pelicula));
                document.add(new Paragraph("Horario: " + horario));
            }

            PdfPTable table = new PdfPTable(3);
            table.addCell("Concepto");
            table.addCell("Detalle");
            table.addCell("Precio");

            for (LineaCompra linea : compra.getLineaCompras()) {
                if (linea.getEntrada() != null) {
                    Entrada e = linea.getEntrada();
                    table.addCell("Entrada " + e.getTipo().getNombre());
                    table.addCell("Fila " + e.getId().getFila() + ", Butaca " + e.getId().getButaca());
                    table.addCell(String.format("%.2f EUR", e.getTipo().getPrecio().doubleValue()));
                } else if (linea.getProducto() != null) {
                    Producto p = linea.getProducto();
                    table.addCell("Producto");
                    table.addCell(p.getNombre());
                    table.addCell(String.format("%.2f EUR", p.getPrecio()));
                }
            }

            document.add(table);
            document.add(new Paragraph(" "));

            Image qrImage = crearQrImage(compra.getId().toString());
            if (qrImage != null) {
                qrImage.scaleToFit(150, 150);
                document.add(qrImage);
            } else {
                document.add(new Paragraph("QR no disponible"));
            }

            document.close();
            return out.toByteArray();
        } catch (IOException | DocumentException e) {
            throw new RuntimeException("Error generando PDF de compra", e);
        }
    }

    private Image crearQrImage(String text) {
        try (ByteArrayOutputStream pngOut = new ByteArrayOutputStream()) {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, 200, 200);
            BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);
            ImageIO.write(image, "PNG", pngOut);
            return Image.getInstance(pngOut.toByteArray());
        } catch (WriterException | IOException | DocumentException e) {
            return null;
        }
    }
}

