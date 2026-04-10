package com.example.hospitalsystem.service;

import com.example.hospitalsystem.dto.paciente.PacienteRequest;
import com.example.hospitalsystem.dto.paciente.PacienteResponse;
import com.example.hospitalsystem.exception.DuplicateResourceException;
import com.example.hospitalsystem.exception.ResourceNotFoundException;
import com.example.hospitalsystem.model.AntecedenteMedico;
import com.example.hospitalsystem.model.HistoriaClinica;
import com.example.hospitalsystem.model.Paciente;
import com.example.hospitalsystem.repository.AntecedenteMedicoRepository;
import com.example.hospitalsystem.repository.HistoriaClinicaRepository;
import com.example.hospitalsystem.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private HistoriaClinicaRepository historiaClinicaRepository;

    @Autowired
    private AntecedenteMedicoRepository antecedenteMedicoRepository;

    // ---- Mapeo DTO ----

    public PacienteResponse toResponse(Paciente p) {
        return PacienteResponse.builder()
                .idPaciente(p.getIdPaciente())
                .dni(p.getDni())
                .nombres(p.getNombres())
                .apellidos(p.getApellidos())
                .nombreCompleto(p.getNombres() + " " + p.getApellidos())
                .fechaNacimiento(p.getFechaNacimiento())
                .sexo(p.getSexo())
                .direccion(p.getDireccion())
                .telefono(p.getTelefono())
                .correo(p.getCorreo())
                .estado(p.getEstado())
                .build();
    }

    private Paciente toEntity(PacienteRequest req) {
        Paciente p = new Paciente();
        p.setDni(req.getDni());
        p.setNombres(req.getNombres());
        p.setApellidos(req.getApellidos());
        p.setFechaNacimiento(req.getFechaNacimiento());
        p.setSexo(req.getSexo());
        p.setDireccion(req.getDireccion());
        p.setTelefono(req.getTelefono());
        p.setCorreo(req.getCorreo());
        p.setEstado(req.getEstado() != null ? req.getEstado() : "activo");
        return p;
    }

    // ---- Operaciones ----

    public List<PacienteResponse> getAllPacientes() {
        return pacienteRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Page<PacienteResponse> searchPacientes(String search, String estado, Pageable pageable) {
        return pacienteRepository.search(search, estado, pageable)
                .map(this::toResponse);
    }

    public PacienteResponse getPacienteById(Long id) {
        return pacienteRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", id));
    }

    public Optional<PacienteResponse> getPacienteByDni(String dni) {
        return pacienteRepository.findByDni(dni).map(this::toResponse);
    }

    @Transactional
    public PacienteResponse createPaciente(PacienteRequest request) {
        if (pacienteRepository.findByDni(request.getDni()).isPresent()) {
            throw new DuplicateResourceException("Ya existe un paciente con el DNI: " + request.getDni());
        }

        Paciente saved = pacienteRepository.save(toEntity(request));

        // Crear historia clínica automáticamente
        HistoriaClinica historia = new HistoriaClinica();
        historia.setIdPaciente(saved.getIdPaciente());
        historia.setFechaApertura(LocalDate.now());
        historia.setObservaciones("Historia clínica creada automáticamente");
        historiaClinicaRepository.save(historia);

        return toResponse(saved);
    }

    public PacienteResponse updatePaciente(Long id, PacienteRequest request) {
        Paciente existing = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", id));

        // Verificar DNI duplicado (solo si cambió)
        if (!existing.getDni().equals(request.getDni()) &&
                pacienteRepository.findByDni(request.getDni()).isPresent()) {
            throw new DuplicateResourceException("Ya existe un paciente con el DNI: " + request.getDni());
        }

        Paciente updated = toEntity(request);
        updated.setIdPaciente(id);
        return toResponse(pacienteRepository.save(updated));
    }

    public void deletePaciente(Long id) {
        if (!pacienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Paciente", id);
        }
        pacienteRepository.deleteById(id);
    }

    public Optional<HistoriaClinica> getHistoriaClinicaByPaciente(Long idPaciente) {
        return historiaClinicaRepository.findByIdPaciente(idPaciente);
    }

    public List<AntecedenteMedico> getAntecedentesByHistoria(Long idHistoria) {
        return antecedenteMedicoRepository.findByIdHistoria(idHistoria);
    }

    public AntecedenteMedico addAntecedenteMedico(AntecedenteMedico antecedente) {
        return antecedenteMedicoRepository.save(antecedente);
    }
}
