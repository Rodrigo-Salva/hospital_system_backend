package com.example.hospitalsystem.repository;


import com.example.hospitalsystem.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Optional<Paciente> findByDni(String dni);
    List<Paciente> findByEstado(String estado);
    List<Paciente> findByNombresContainingOrApellidosContaining(String nombres, String apellidos);
}
