package com.example.hospitalsystem.dto.consulta;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ConsultaRequest {

    @NotNull(message = "La cita es obligatoria")
    private Long idCita;

    @NotNull(message = "El médico es obligatorio")
    private Long idMedico;

    @NotNull(message = "El paciente es obligatorio")
    private Long idPaciente;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotNull(message = "La hora es obligatoria")
    private LocalTime hora;

    @NotBlank(message = "El motivo de consulta es obligatorio")
    private String motivoConsulta;

    private String observaciones;
}
