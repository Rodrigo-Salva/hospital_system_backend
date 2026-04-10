package com.example.hospitalsystem.dto.medico;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MedicoResponse {
    private Long idMedico;
    private String nombres;
    private String apellidos;
    private String nombreCompleto;
    private String colegiatura;
    private String telefono;
    private String correo;
    private String estado;
}
