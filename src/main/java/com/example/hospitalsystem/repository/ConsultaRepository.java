package com.example.hospitalsystem.repository;

import com.example.hospitalsystem.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    List<Consulta> findByIdPaciente(Long idPaciente);
    List<Consulta> findByIdMedico(Long idMedico);
    List<Consulta> findByIdCita(Long idCita);
}
