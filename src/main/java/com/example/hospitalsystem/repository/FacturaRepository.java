package com.example.hospitalsystem.repository;

import com.example.hospitalsystem.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {
    List<Factura> findByIdPaciente(Long idPaciente);
    List<Factura> findByEstado(String estado);
}
