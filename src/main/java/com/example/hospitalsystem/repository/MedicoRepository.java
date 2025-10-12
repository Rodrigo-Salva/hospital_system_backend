package com.example.hospitalsystem.repository;

import com.example.hospitalsystem.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {
    Optional<Medico> findByColegiatura(String colegiatura);
    List<Medico> findByEstado(String estado);
}
