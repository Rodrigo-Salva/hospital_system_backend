package com.example.hospitalsystem.repository;

import com.example.hospitalsystem.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    List<Cita> findByIdPaciente(Long idPaciente);
    List<Cita> findByIdMedico(Long idMedico);
    List<Cita> findByEstado(String estado);
    List<Cita> findByFecha(LocalDate fecha);
    List<Cita> findByIdMedicoAndFecha(Long idMedico, LocalDate fecha);
}
