package com.example.hospitalsystem.dto.cita;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CitaRequest {

    @NotNull(message = "El paciente es obligatorio")
    private Long idPaciente;

    @NotNull(message = "El médico es obligatorio")
    private Long idMedico;

    @NotNull(message = "La fecha es obligatoria")
    @FutureOrPresent(message = "La fecha de la cita no puede ser en el pasado")
    private LocalDate fecha;

    @NotNull(message = "La hora es obligatoria")
    private LocalTime hora;

    @NotBlank(message = "El motivo es obligatorio")
    private String motivo;

    private String estado = "programada";
}
