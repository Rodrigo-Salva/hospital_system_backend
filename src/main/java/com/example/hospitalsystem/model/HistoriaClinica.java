package com.example.hospitalsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "historias_clinicas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoriaClinica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHistoria;

    @Column(nullable = false)
    private Long idPaciente;

    @Column(nullable = false)
    private LocalDate fechaApertura;

    @Column(columnDefinition = "TEXT")
    private String observaciones;
}
