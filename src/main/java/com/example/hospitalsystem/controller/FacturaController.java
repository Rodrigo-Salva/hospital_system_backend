package com.example.hospitalsystem.controller;

import com.example.hospitalsystem.model.DetalleFactura;
import com.example.hospitalsystem.model.Factura;
import com.example.hospitalsystem.model.Paciente;
import com.example.hospitalsystem.service.FacturaService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/facturas")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    // ===============================
    // FACTURAS CRUD
    // ===============================

    @GetMapping
    public List<Factura> getAllFacturas() {
        return facturaService.getAllFacturas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Factura> getFacturaById(@PathVariable Long id) {
        Optional<Factura> facturaOpt = facturaService.getFacturaById(id);
        return facturaOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Factura> createFactura(@RequestBody Factura factura) {
        Factura created = facturaService.createFactura(factura);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Factura> updateFactura(@PathVariable Long id, @RequestBody Factura factura) {
        Factura updated = facturaService.updateFactura(id, factura);
        return (updated != null) ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFactura(@PathVariable Long id) {
        if (facturaService.deleteFactura(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ===============================
    // DETALLES
    // ===============================

    @GetMapping("/{idFactura}/detalles")
    public List<DetalleFactura> getDetallesByFactura(@PathVariable Long idFactura) {
        return facturaService.getDetallesByFactura(idFactura);
    }

    @PostMapping("/{idFactura}/detalles")
    public ResponseEntity<DetalleFactura> addDetalle(@PathVariable Long idFactura, @RequestBody DetalleFactura detalle) {
        detalle.setIdFactura(idFactura);
        DetalleFactura saved = facturaService.addDetalle(detalle);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // ===============================
    // EXPORTAR PDF (versión estilizada)
    // ===============================

    @GetMapping("/{idFactura}/pdf")
    public ResponseEntity<byte[]> exportFacturaPDF(@PathVariable Long idFactura) {
        Optional<Factura> facturaOpt = facturaService.getFacturaById(idFactura);
        if (facturaOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Factura factura = facturaOpt.get();
        Optional<Paciente> pacienteOpt = facturaService.getPacienteById(factura.getIdPaciente());
        String nombrePaciente = pacienteOpt.map(Paciente::getNombres).orElse("Desconocido");

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter.getInstance(document, out);
            document.open();

            // ===========================
            // ENCABEZADO
            // ===========================
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, new BaseColor(0, 102, 204));
            Font subTitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.DARK_GRAY);

            Paragraph title = new Paragraph("🏥  Factura Hospitalaria", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(10f);
            document.add(title);

            LineSeparator separator = new LineSeparator();
            separator.setLineColor(new BaseColor(200, 200, 200));
            document.add(separator);
            document.add(Chunk.NEWLINE);

            // ===========================
            // DATOS GENERALES
            // ===========================
            Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(90);
            infoTable.setSpacingBefore(10f);
            infoTable.setHorizontalAlignment(Element.ALIGN_CENTER);
            infoTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            infoTable.addCell(new Phrase("ID Factura:", labelFont));
            infoTable.addCell(new Phrase(String.valueOf(factura.getIdFactura()), valueFont));

            infoTable.addCell(new Phrase("Paciente:", labelFont));
            infoTable.addCell(new Phrase(nombrePaciente, valueFont));

            infoTable.addCell(new Phrase("Fecha de Emisión:", labelFont));
            infoTable.addCell(new Phrase(String.valueOf(factura.getFechaEmision()), valueFont));

            infoTable.addCell(new Phrase("Estado:", labelFont));
            infoTable.addCell(new Phrase(String.valueOf(factura.getEstado()), valueFont));

            infoTable.addCell(new Phrase("Descripción:", labelFont));
            infoTable.addCell(new Phrase(
                    (factura.getDescripcion() != null ? factura.getDescripcion() : "N/A"), valueFont));

            document.add(infoTable);
            document.add(Chunk.NEWLINE);

            // ===========================
            // TABLA DETALLES
            // ===========================
            Paragraph detallesTitle = new Paragraph("Detalles de la Factura", subTitleFont);
            detallesTitle.setAlignment(Element.ALIGN_LEFT);
            detallesTitle.setSpacingAfter(5f);
            document.add(detallesTitle);

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(90);
            table.setSpacingBefore(5f);
            table.setSpacingAfter(10f);
            table.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell header1 = new PdfPCell(new Phrase("Concepto", labelFont));
            PdfPCell header2 = new PdfPCell(new Phrase("Monto (S/)", labelFont));

            BaseColor headerColor = new BaseColor(230, 230, 250);
            header1.setBackgroundColor(headerColor);
            header2.setBackgroundColor(headerColor);

            header1.setHorizontalAlignment(Element.ALIGN_CENTER);
            header2.setHorizontalAlignment(Element.ALIGN_CENTER);

            table.addCell(header1);
            table.addCell(header2);

            List<DetalleFactura> detalles = facturaService.getDetallesByFactura(factura.getIdFactura());
            if (detalles.isEmpty()) {
                PdfPCell cell = new PdfPCell(new Phrase("Sin detalles registrados", valueFont));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(10f);
                table.addCell(cell);
            } else {
                for (DetalleFactura det : detalles) {
                    PdfPCell c1 = new PdfPCell(new Phrase(det.getConcepto(), valueFont));
                    PdfPCell c2 = new PdfPCell(new Phrase(det.getMonto().toString(), valueFont));
                    c1.setPadding(6f);
                    c2.setPadding(6f);
                    table.addCell(c1);
                    table.addCell(c2);
                }
            }

            document.add(table);

            // ===========================
            // TOTAL
            // ===========================
            BigDecimal total = factura.getTotal() != null ? factura.getTotal() : BigDecimal.ZERO;
            Paragraph totalPar = new Paragraph("Total: S/ " + total, labelFont);
            totalPar.setAlignment(Element.ALIGN_RIGHT);
            totalPar.setSpacingBefore(15f);
            document.add(totalPar);

            // ===========================
            // PIE DE PÁGINA
            // ===========================
            document.add(Chunk.NEWLINE);
            LineSeparator footerLine = new LineSeparator();
            footerLine.setLineColor(new BaseColor(220, 220, 220));
            document.add(footerLine);
            document.add(Chunk.NEWLINE);

            Font footerFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10, new BaseColor(100, 100, 100));
            Paragraph footer = new Paragraph("Gracias por confiar en el Hospital Central", footerFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();

            byte[] pdfBytes = out.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "factura_" + idFactura + ".pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
