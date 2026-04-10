package com.example.hospitalsystem.dto.paciente;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class PacienteResponse {
    private Long idPaciente;
    private String dni;
    private String nombres;
    private String apellidos;
    private String nombreCompleto;
    private LocalDate fechaNacimiento;
    private String sexo;
    private String direccion;
    private String telefono;
    private String correo;
    private String estado;
}
