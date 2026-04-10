package com.example.hospitalsystem.dto.factura;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class FacturaResponse {
    private Long idFactura;
    private Long idPaciente;
    private String pacienteNombre;
    private LocalDate fechaEmision;
    private BigDecimal total;
    private String estado;
    private String descripcion;
}
