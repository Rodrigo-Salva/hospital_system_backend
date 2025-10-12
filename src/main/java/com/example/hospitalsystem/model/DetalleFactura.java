package com.example.hospitalsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "detalle_facturas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleFactura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalleFactura;

    @Column(nullable = false)
    private Long idFactura;

    @Column(nullable = false, length = 200)
    private String concepto; // consulta, medicamento, procedimiento

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;
}
