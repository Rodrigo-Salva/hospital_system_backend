package com.example.hospitalsystem.dto.consulta;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class ConsultaResponse {
    private Long idConsulta;
    private Long idCita;
    private Long idMedico;
    private String medicoNombre;
    private Long idPaciente;
    private String pacienteNombre;
    private LocalDate fecha;
    private LocalTime hora;
    private String motivoConsulta;
    private String observaciones;
}
