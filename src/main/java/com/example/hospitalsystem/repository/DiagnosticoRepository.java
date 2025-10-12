package com.example.hospitalsystem.repository;

import com.example.hospitalsystem.model.Diagnostico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiagnosticoRepository extends JpaRepository<Diagnostico, Long> {
    List<Diagnostico> findByIdConsulta(Long idConsulta);
}
