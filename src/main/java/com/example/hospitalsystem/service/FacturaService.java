package com.example.hospitalsystem.service;

import com.example.hospitalsystem.dto.factura.FacturaRequest;
import com.example.hospitalsystem.dto.factura.FacturaResponse;
import com.example.hospitalsystem.exception.ResourceNotFoundException;
import com.example.hospitalsystem.model.DetalleFactura;
import com.example.hospitalsystem.model.Factura;
import com.example.hospitalsystem.model.Paciente;
import com.example.hospitalsystem.repository.DetalleFacturaRepository;
import com.example.hospitalsystem.repository.FacturaRepository;
import com.example.hospitalsystem.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private DetalleFacturaRepository detalleFacturaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    // ---- Mapeo DTO ----

    public FacturaResponse toResponse(Factura f) {
        String pacienteNombre = pacienteRepository.findById(f.getIdPaciente())
                .map(p -> p.getNombres() + " " + p.getApellidos())
                .orElse("Desconocido");

        return FacturaResponse.builder()
                .idFactura(f.getIdFactura())
                .idPaciente(f.getIdPaciente())
                .pacienteNombre(pacienteNombre)
                .fechaEmision(f.getFechaEmision())
                .total(f.getTotal())
                .estado(f.getEstado())
                .descripcion(f.getDescripcion())
                .build();
    }

    private Factura toEntity(FacturaRequest req) {
        Factura f = new Factura();
        f.setIdPaciente(req.getIdPaciente());
        f.setFechaEmision(req.getFechaEmision());
        f.setTotal(req.getTotal() != null ? req.getTotal() : BigDecimal.ZERO);
        f.setEstado(req.getEstado() != null ? req.getEstado() : "pendiente");
        if (req.getDescripcion() != null && !req.getDescripcion().trim().isEmpty()) {
            f.setDescripcion(req.getDescripcion());
        }
        return f;
    }

    // ---- Facturas ----

    public List<FacturaResponse> getAllFacturas() {
        return facturaRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Page<FacturaResponse> searchFacturas(String estado, Pageable pageable) {
        return facturaRepository.search(estado, pageable)
                .map(this::toResponse);
    }

    public Optional<Factura> getFacturaEntityById(Long id) {
        return facturaRepository.findById(id);
    }

    public Optional<FacturaResponse> getFacturaById(Long id) {
        return facturaRepository.findById(id).map(this::toResponse);
    }

    public List<FacturaResponse> getFacturasByPaciente(Long idPaciente) {
        return facturaRepository.findByIdPaciente(idPaciente).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<FacturaResponse> getFacturasByEstado(String estado) {
        return facturaRepository.findByEstado(estado).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public FacturaResponse createFactura(FacturaRequest request) {
        return toResponse(facturaRepository.save(toEntity(request)));
    }

    public FacturaResponse updateFactura(Long id, FacturaRequest request) {
        Factura existing = facturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Factura", id));
        existing.setIdPaciente(request.getIdPaciente());
        existing.setFechaEmision(request.getFechaEmision());
        if (request.getTotal() != null) existing.setTotal(request.getTotal());
        existing.setEstado(request.getEstado() != null ? request.getEstado() : existing.getEstado());
        existing.setDescripcion(request.getDescripcion());
        return toResponse(facturaRepository.save(existing));
    }

    public void deleteFactura(Long id) {
        if (!facturaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Factura", id);
        }
        facturaRepository.deleteById(id);
    }

    // ---- Detalles ----

    public List<DetalleFactura> getDetallesByFactura(Long idFactura) {
        return detalleFacturaRepository.findByIdFactura(idFactura);
    }

    @Transactional
    public DetalleFactura addDetalle(DetalleFactura detalle) {
        DetalleFactura saved = detalleFacturaRepository.save(detalle);

        // Recalcular total de la factura
        List<DetalleFactura> detalles = detalleFacturaRepository.findByIdFactura(detalle.getIdFactura());
        BigDecimal total = detalles.stream()
                .map(DetalleFactura::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        facturaRepository.findById(detalle.getIdFactura()).ifPresent(factura -> {
            factura.setTotal(total);
            facturaRepository.save(factura);
        });

        return saved;
    }

    // Para uso del FacturaController al generar PDF
    public Optional<Paciente> getPacienteById(Long idPaciente) {
        return pacienteRepository.findById(idPaciente);
    }
}
