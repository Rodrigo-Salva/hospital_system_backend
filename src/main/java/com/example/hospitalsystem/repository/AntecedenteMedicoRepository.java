package com.example.hospitalsystem.repository;

import com.example.hospitalsystem.model.AntecedenteMedico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AntecedenteMedicoRepository extends JpaRepository<AntecedenteMedico, Long> {
    List<AntecedenteMedico> findByIdHistoria(Long idHistoria);
    List<AntecedenteMedico> findByIdHistoriaAndTipo(Long idHistoria, String tipo);
}
