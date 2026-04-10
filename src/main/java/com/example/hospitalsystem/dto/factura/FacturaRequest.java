package com.example.hospitalsystem.dto.factura;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class FacturaRequest {

    @NotNull(message = "El paciente es obligatorio")
    private Long idPaciente;

    @NotNull(message = "La fecha de emisión es obligatoria")
    private LocalDate fechaEmision;

    private BigDecimal total;

    @Pattern(regexp = "pendiente|pagado", message = "El estado debe ser 'pendiente' o 'pagado'")
    private String estado = "pendiente";

    private String descripcion;
}
