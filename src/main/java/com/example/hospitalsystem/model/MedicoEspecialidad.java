package com.example.hospitalsystem.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "medico_especialidad")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicoEspecialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMedicoEsp;

    @Column(nullable = false)
    private Long idMedico;

    @Column(nullable = false)
    private Long idEspecialidad;
}
