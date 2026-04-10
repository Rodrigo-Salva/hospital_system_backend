package com.example.hospitalsystem.dto.cita;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class CitaResponse {
    private Long idCita;
    private Long idPaciente;
    private String pacienteNombre;
    private Long idMedico;
    private String medicoNombre;
    private LocalDate fecha;
    private LocalTime hora;
    private String motivo;
    private String estado;
}
