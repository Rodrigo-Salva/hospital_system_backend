package com.example.hospitalsystem.dto.medico;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class MedicoRequest {

    @NotBlank(message = "Los nombres son obligatorios")
    @Size(max = 100, message = "Los nombres no pueden superar 100 caracteres")
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 100, message = "Los apellidos no pueden superar 100 caracteres")
    private String apellidos;

    @NotBlank(message = "La colegiatura es obligatoria")
    @Size(max = 20, message = "La colegiatura no puede superar 20 caracteres")
    private String colegiatura;

    @Size(max = 15, message = "El teléfono no puede superar 15 caracteres")
    private String telefono;

    @Email(message = "El correo no tiene un formato válido")
    @Size(max = 100, message = "El correo no puede superar 100 caracteres")
    private String correo;

    private String estado = "activo";
}
