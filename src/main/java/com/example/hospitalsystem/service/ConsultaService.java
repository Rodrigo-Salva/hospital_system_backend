package com.example.hospitalsystem.service;

import com.example.hospitalsystem.dto.consulta.ConsultaRequest;
import com.example.hospitalsystem.dto.consulta.ConsultaResponse;
import com.example.hospitalsystem.exception.ResourceNotFoundException;
import com.example.hospitalsystem.model.Consulta;
import com.example.hospitalsystem.model.Diagnostico;
import com.example.hospitalsystem.repository.ConsultaRepository;
import com.example.hospitalsystem.repository.DiagnosticoRepository;
import com.example.hospitalsystem.repository.MedicoRepository;
import com.example.hospitalsystem.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private DiagnosticoRepository diagnosticoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    // ---- Mapeo DTO ----

    public ConsultaResponse toResponse(Consulta c) {
        String pacienteNombre = pacienteRepository.findById(c.getIdPaciente())
                .map(p -> p.getNombres() + " " + p.getApellidos())
                .orElse("Desconocido");

        String medicoNombre = medicoRepository.findById(c.getIdMedico())
                .map(m -> "Dr. " + m.getNombres() + " " + m.getApellidos())
                .orElse("Desconocido");

        return ConsultaResponse.builder()
                .idConsulta(c.getIdConsulta())
                .idCita(c.getIdCita())
                .idMedico(c.getIdMedico())
                .medicoNombre(medicoNombre)
                .idPaciente(c.getIdPaciente())
                .pacienteNombre(pacienteNombre)
                .fecha(c.getFecha())
                .hora(c.getHora())
                .motivoConsulta(c.getMotivoConsulta())
                .observaciones(c.getObservaciones())
                .build();
    }

    private Consulta toEntity(ConsultaRequest req) {
        Consulta c = new Consulta();
        c.setIdCita(req.getIdCita());
        c.setIdMedico(req.getIdMedico());
        c.setIdPaciente(req.getIdPaciente());
        c.setFecha(req.getFecha());
        c.setHora(req.getHora());
        c.setMotivoConsulta(req.getMotivoConsulta());
        c.setObservaciones(req.getObservaciones());
        return c;
    }

    // ---- Operaciones ----

    public List<ConsultaResponse> getAllConsultas() {
        return consultaRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Optional<ConsultaResponse> getConsultaById(Long id) {
        return consultaRepository.findById(id).map(this::toResponse);
    }

    public List<ConsultaResponse> getConsultasByPaciente(Long idPaciente) {
        return consultaRepository.findByIdPaciente(idPaciente).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<ConsultaResponse> getConsultasByMedico(Long idMedico) {
        return consultaRepository.findByIdMedico(idMedico).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ConsultaResponse createConsulta(ConsultaRequest request) {
        return toResponse(consultaRepository.save(toEntity(request)));
    }

    public ConsultaResponse updateConsulta(Long id, ConsultaRequest request) {
        if (!consultaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Consulta", id);
        }
        Consulta updated = toEntity(request);
        updated.setIdConsulta(id);
        return toResponse(consultaRepository.save(updated));
    }

    public void deleteConsulta(Long id) {
        if (!consultaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Consulta", id);
        }
        consultaRepository.deleteById(id);
    }

    public List<Diagnostico> getDiagnosticosByConsulta(Long idConsulta) {
        return diagnosticoRepository.findByIdConsulta(idConsulta);
    }

    public Diagnostico addDiagnostico(Diagnostico diagnostico) {
        return diagnosticoRepository.save(diagnostico);
    }
}
