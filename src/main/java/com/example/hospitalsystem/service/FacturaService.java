package com.example.hospitalsystem.service;

import com.example.hospitalsystem.model.DetalleFactura;
import com.example.hospitalsystem.model.Factura;
import com.example.hospitalsystem.model.Paciente;
import com.example.hospitalsystem.repository.DetalleFacturaRepository;
import com.example.hospitalsystem.repository.FacturaRepository;
import com.example.hospitalsystem.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private DetalleFacturaRepository detalleFacturaRepository;

    public List<Factura> getAllFacturas() {
        return facturaRepository.findAll();
    }

    public Optional<Factura> getFacturaById(Long id) {
        return facturaRepository.findById(id);
    }

    public List<Factura> getFacturasByPaciente(Long idPaciente) {
        return facturaRepository.findByIdPaciente(idPaciente);
    }

    public Factura createFactura(Factura factura) {
        return facturaRepository.save(factura);
    }

    public Factura updateFactura(Long id, Factura factura) {
        if (facturaRepository.existsById(id)) {
            factura.setIdFactura(id);
            return facturaRepository.save(factura);
        }
        return null;
    }

    @Transactional
    public Factura cambiarEstado(Long id, String nuevoEstado) {
        Optional<Factura> facturaOpt = facturaRepository.findById(id);
        if (facturaOpt.isPresent()) {
            Factura factura = facturaOpt.get();
            factura.setEstado(nuevoEstado);
            return facturaRepository.save(factura);
        }
        return null;
    }

    public boolean deleteFactura(Long id) {
        if (facturaRepository.existsById(id)) {
            facturaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Métodos para Detalles
    public List<DetalleFactura> getDetallesByFactura(Long idFactura) {
        return detalleFacturaRepository.findByIdFactura(idFactura);
    }

    @Transactional
    public DetalleFactura addDetalle(DetalleFactura detalle) {
        DetalleFactura savedDetalle = detalleFacturaRepository.save(detalle);

        // Recalcular el total de la factura
        List<DetalleFactura> detalles = detalleFacturaRepository.findByIdFactura(detalle.getIdFactura());
        BigDecimal total = detalles.stream()
                .map(DetalleFactura::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Optional<Factura> facturaOpt = facturaRepository.findById(detalle.getIdFactura());
        if (facturaOpt.isPresent()) {
            Factura factura = facturaOpt.get();
            factura.setTotal(total);
            facturaRepository.save(factura);
        }

        return savedDetalle;
    }

    @Autowired
    private PacienteRepository pacienteRepository;

    public Optional<Paciente> getPacienteById(Long idPaciente) {
        return pacienteRepository.findById(idPaciente);
    }

}
