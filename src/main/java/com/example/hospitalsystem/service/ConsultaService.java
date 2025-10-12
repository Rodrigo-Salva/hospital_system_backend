package com.example.hospitalsystem.service;


import com.example.hospitalsystem.model.Consulta;
import com.example.hospitalsystem.model.Diagnostico;
import com.example.hospitalsystem.repository.ConsultaRepository;
import com.example.hospitalsystem.repository.DiagnosticoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private DiagnosticoRepository diagnosticoRepository;

    public List<Consulta> getAllConsultas() {
        return consultaRepository.findAll();
    }

    public Optional<Consulta> getConsultaById(Long id) {
        return consultaRepository.findById(id);
    }

    public List<Consulta> getConsultasByPaciente(Long idPaciente) {
        return consultaRepository.findByIdPaciente(idPaciente);
    }

    public List<Consulta> getConsultasByMedico(Long idMedico) {
        return consultaRepository.findByIdMedico(idMedico);
    }

    public Consulta createConsulta(Consulta consulta) {
        return consultaRepository.save(consulta);
    }

    public Consulta updateConsulta(Long id, Consulta consulta) {
        if (consultaRepository.existsById(id)) {
            consulta.setIdConsulta(id);
            return consultaRepository.save(consulta);
        }
        return null;
    }

    public boolean deleteConsulta(Long id) {
        if (consultaRepository.existsById(id)) {
            consultaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Métodos para Diagnósticos
    public List<Diagnostico> getDiagnosticosByConsulta(Long idConsulta) {
        return diagnosticoRepository.findByIdConsulta(idConsulta);
    }

    public Diagnostico addDiagnostico(Diagnostico diagnostico) {
        return diagnosticoRepository.save(diagnostico);
    }
}
