package com.example.hospitalsystem.repository;

import com.example.hospitalsystem.model.Factura;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {
    List<Factura> findByIdPaciente(Long idPaciente);
    List<Factura> findByEstado(String estado);

    @Query("SELECT f FROM Factura f WHERE " +
           "(:estado IS NULL OR f.estado = :estado)")
    Page<Factura> search(@Param("estado") String estado, Pageable pageable);

    long countByEstado(String estado);

    @Query("SELECT COALESCE(SUM(f.total), 0) FROM Factura f WHERE " +
           "MONTH(f.fechaEmision) = :mes AND f.estado = 'pagado'")
    BigDecimal sumTotalByMes(@Param("mes") int mes);
}
