package com.example.hospitalsystem.controller;

import com.example.hospitalsystem.model.DetalleFactura;
import com.example.hospitalsystem.model.Factura;
import com.example.hospitalsystem.model.Paciente;
import com.example.hospitalsystem.service.FacturaService;
import com.itextpdf.text.pdf.PdfPTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.io.ByteArrayOutputStream;



import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/facturas")
@CrossOrigin(origins = "http://localhost:3000")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    @GetMapping
    public ResponseEntity<List<Factura>> getAllFacturas() {
        return ResponseEntity.ok(facturaService.getAllFacturas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Factura> getFacturaById(@PathVariable Long id) {
        return facturaService.getFacturaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/paciente/{idPaciente}")
    public ResponseEntity<List<Factura>> getFacturasByPaciente(@PathVariable Long idPaciente) {
        return ResponseEntity.ok(facturaService.getFacturasByPaciente(idPaciente));
    }

    @PostMapping
    public ResponseEntity<Factura> createFactura(@RequestBody Factura factura) {
        return ResponseEntity.status(HttpStatus.CREATED).body(facturaService.createFactura(factura));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Factura> updateFactura(@PathVariable Long id, @RequestBody Factura factura) {
        Factura updatedFactura = facturaService.updateFactura(id, factura);
        if (updatedFactura != null) {
            return ResponseEntity.ok(updatedFactura);
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Factura> cambiarEstado(@PathVariable Long id,
                                                 @RequestBody Map<String, String> body) {
        String nuevoEstado = body.get("estado");
        Factura updatedFactura = facturaService.cambiarEstado(id, nuevoEstado);
        if (updatedFactura != null) {
            return ResponseEntity.ok(updatedFactura);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFactura(@PathVariable Long id) {
        if (facturaService.deleteFactura(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Endpoints para Detalles
    @GetMapping("/{idFactura}/detalles")
    public ResponseEntity<List<DetalleFactura>> getDetallesByFactura(@PathVariable Long idFactura) {
        return ResponseEntity.ok(facturaService.getDetallesByFactura(idFactura));
    }

    @PostMapping("/{idFactura}/detalles")
    public ResponseEntity<DetalleFactura> addDetalle(@PathVariable Long idFactura,
                                                     @RequestBody DetalleFactura detalle) {
        detalle.setIdFactura(idFactura);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(facturaService.addDetalle(detalle));
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> generarFacturaPDF(@PathVariable Long id) {
        try {
            Factura factura = facturaService.getFacturaById(id)
                    .orElseThrow(() -> new RuntimeException("Factura no encontrada"));

            // Obtener paciente y detalles
            Paciente paciente = facturaService.getPacienteById(factura.getIdPaciente())
                    .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
            List<DetalleFactura> detalles = facturaService.getDetallesByFactura(factura.getIdFactura());

            // Crear PDF en memoria
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            // Título
            Font tituloFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph titulo = new Paragraph("Factura Hospitalaria", tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);
            document.add(new Paragraph(" "));

            // Datos del paciente
            document.add(new Paragraph("Paciente: " + paciente.getNombres() + " " + paciente.getApellidos()));
            document.add(new Paragraph("DNI: " + paciente.getDni()));
            document.add(new Paragraph("Correo: " + (paciente.getCorreo() != null ? paciente.getCorreo() : "N/A")));
            document.add(new Paragraph("Teléfono: " + (paciente.getTelefono() != null ? paciente.getTelefono() : "N/A")));
            document.add(new Paragraph("Fecha de Emisión: " + factura.getFechaEmision()));
            document.add(new Paragraph("Estado: " + factura.getEstado()));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Detalles de la factura:"));
            document.add(new Paragraph(" "));

            // Tabla de detalles
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.addCell("Concepto");
            table.addCell("Monto (S/.)");

            for (DetalleFactura det : detalles) {
                table.addCell(det.getConcepto());
                table.addCell(det.getMonto().toString());
            }

            document.add(table);
            document.add(new Paragraph(" "));

            // Total
            Font totalFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Paragraph total = new Paragraph("Total: S/ " + factura.getTotal(), totalFont);
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);

            document.close();

            // Retornar PDF
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=factura_" + id + ".pdf");
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                    .body(baos.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
