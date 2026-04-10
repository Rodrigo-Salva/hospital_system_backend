package com.example.hospitalsystem.repository;

import com.example.hospitalsystem.model.Paciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Optional<Paciente> findByDni(String dni);
    List<Paciente> findByEstado(String estado);
    List<Paciente> findByNombresContainingOrApellidosContaining(String nombres, String apellidos);

    @Query("SELECT p FROM Paciente p WHERE " +
           "(:search IS NULL OR LOWER(p.nombres) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(p.apellidos) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR p.dni LIKE CONCAT('%', :search, '%')) " +
           "AND (:estado IS NULL OR p.estado = :estado)")
    Page<Paciente> search(@Param("search") String search,
                          @Param("estado") String estado,
                          Pageable pageable);
}
