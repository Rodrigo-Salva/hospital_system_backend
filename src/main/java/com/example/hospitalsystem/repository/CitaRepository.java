package com.example.hospitalsystem.repository;

import com.example.hospitalsystem.model.Cita;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT c FROM Cita c WHERE " +
           "(:estado IS NULL OR c.estado = :estado) " +
           "AND (:fecha IS NULL OR c.fecha = :fecha)")
    Page<Cita> search(@Param("estado") String estado,
                      @Param("fecha") LocalDate fecha,
                      Pageable pageable);

    long countByEstado(String estado);
    long countByFecha(LocalDate fecha);
}
