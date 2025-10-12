package com.example.hospitalsystem.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "antecedentes_medicos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AntecedenteMedico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAntecedente;

    @Column(nullable = false)
    private Long idHistoria;

    @Column(nullable = false, length = 50)
    private String tipo; // alergias, enfermedades previas, cirugías, otros

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;
}
