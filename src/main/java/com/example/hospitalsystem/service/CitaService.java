package com.example.hospitalsystem.service;

import com.example.hospitalsystem.dto.cita.CitaRequest;
import com.example.hospitalsystem.dto.cita.CitaResponse;
import com.example.hospitalsystem.exception.ResourceNotFoundException;
import com.example.hospitalsystem.model.Cita;
import com.example.hospitalsystem.repository.CitaRepository;
import com.example.hospitalsystem.repository.MedicoRepository;
import com.example.hospitalsystem.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CitaService {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    // ---- Mapeo DTO ----

    public CitaResponse toResponse(Cita c) {
        String pacienteNombre = pacienteRepository.findById(c.getIdPaciente())
                .map(p -> p.getNombres() + " " + p.getApellidos())
                .orElse("Desconocido");

        String medicoNombre = medicoRepository.findById(c.getIdMedico())
                .map(m -> "Dr. " + m.getNombres() + " " + m.getApellidos())
                .orElse("Desconocido");

        return CitaResponse.builder()
                .idCita(c.getIdCita())
                .idPaciente(c.getIdPaciente())
                .pacienteNombre(pacienteNombre)
                .idMedico(c.getIdMedico())
                .medicoNombre(medicoNombre)
                .fecha(c.getFecha())
                .hora(c.getHora())
                .motivo(c.getMotivo())
                .estado(c.getEstado())
                .build();
    }

    private Cita toEntity(CitaRequest req) {
        Cita c = new Cita();
        c.setIdPaciente(req.getIdPaciente());
        c.setIdMedico(req.getIdMedico());
        c.setFecha(req.getFecha());
        c.setHora(req.getHora());
        c.setMotivo(req.getMotivo());
        c.setEstado(req.getEstado() != null ? req.getEstado() : "programada");
        return c;
    }

    // ---- Operaciones ----

    public List<CitaResponse> getAllCitas() {
        return citaRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Page<CitaResponse> searchCitas(String estado, LocalDate fecha, Pageable pageable) {
        return citaRepository.search(estado, fecha, pageable)
                .map(this::toResponse);
    }

    public Optional<CitaResponse> getCitaById(Long id) {
        return citaRepository.findById(id).map(this::toResponse);
    }

    public List<CitaResponse> getCitasByPaciente(Long idPaciente) {
        return citaRepository.findByIdPaciente(idPaciente).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<CitaResponse> getCitasByMedico(Long idMedico) {
        return citaRepository.findByIdMedico(idMedico).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<CitaResponse> getCitasByFecha(LocalDate fecha) {
        return citaRepository.findByFecha(fecha).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public CitaResponse createCita(CitaRequest request) {
        return toResponse(citaRepository.save(toEntity(request)));
    }

    public CitaResponse updateCita(Long id, CitaRequest request) {
        if (!citaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cita", id);
        }
        Cita updated = toEntity(request);
        updated.setIdCita(id);
        return toResponse(citaRepository.save(updated));
    }

    public CitaResponse cambiarEstado(Long id, String nuevoEstado) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita", id));
        cita.setEstado(nuevoEstado);
        return toResponse(citaRepository.save(cita));
    }

    public void deleteCita(Long id) {
        if (!citaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cita", id);
        }
        citaRepository.deleteById(id);
    }
}
