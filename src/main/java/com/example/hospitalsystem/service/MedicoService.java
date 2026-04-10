package com.example.hospitalsystem.service;

import com.example.hospitalsystem.dto.medico.EspecialidadRequest;
import com.example.hospitalsystem.dto.medico.MedicoRequest;
import com.example.hospitalsystem.dto.medico.MedicoResponse;
import com.example.hospitalsystem.exception.DuplicateResourceException;
import com.example.hospitalsystem.exception.ResourceNotFoundException;
import com.example.hospitalsystem.model.Especialidad;
import com.example.hospitalsystem.model.Medico;
import com.example.hospitalsystem.repository.EspecialidadRepository;
import com.example.hospitalsystem.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private EspecialidadRepository especialidadRepository;

    // ---- Mapeo DTO ----

    public MedicoResponse toResponse(Medico m) {
        return MedicoResponse.builder()
                .idMedico(m.getIdMedico())
                .nombres(m.getNombres())
                .apellidos(m.getApellidos())
                .nombreCompleto(m.getNombres() + " " + m.getApellidos())
                .colegiatura(m.getColegiatura())
                .telefono(m.getTelefono())
                .correo(m.getCorreo())
                .estado(m.getEstado())
                .build();
    }

    private Medico toEntity(MedicoRequest req) {
        Medico m = new Medico();
        m.setNombres(req.getNombres());
        m.setApellidos(req.getApellidos());
        m.setColegiatura(req.getColegiatura());
        m.setTelefono(req.getTelefono());
        m.setCorreo(req.getCorreo());
        m.setEstado(req.getEstado() != null ? req.getEstado() : "activo");
        return m;
    }

    // ---- Médicos ----

    public List<MedicoResponse> getAllMedicos() {
        return medicoRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Page<MedicoResponse> searchMedicos(String search, String estado, Pageable pageable) {
        return medicoRepository.search(search, estado, pageable)
                .map(this::toResponse);
    }

    public Optional<MedicoResponse> getMedicoById(Long id) {
        return medicoRepository.findById(id).map(this::toResponse);
    }

    public MedicoResponse createMedico(MedicoRequest request) {
        if (medicoRepository.findByColegiatura(request.getColegiatura()).isPresent()) {
            throw new DuplicateResourceException("Ya existe un médico con la colegiatura: " + request.getColegiatura());
        }
        return toResponse(medicoRepository.save(toEntity(request)));
    }

    public MedicoResponse updateMedico(Long id, MedicoRequest request) {
        if (!medicoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Médico", id);
        }
        medicoRepository.findByColegiatura(request.getColegiatura()).ifPresent(existing -> {
            if (!existing.getIdMedico().equals(id)) {
                throw new DuplicateResourceException("Otra persona ya tiene esta colegiatura: " + request.getColegiatura());
            }
        });

        Medico updated = toEntity(request);
        updated.setIdMedico(id);
        return toResponse(medicoRepository.save(updated));
    }

    public void deleteMedico(Long id) {
        if (!medicoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Médico", id);
        }
        medicoRepository.deleteById(id);
    }

    // ---- Especialidades ----

    public List<Especialidad> getAllEspecialidades() {
        return especialidadRepository.findAll();
    }

    public Optional<Especialidad> getEspecialidadById(Long id) {
        return especialidadRepository.findById(id);
    }

    public Especialidad createEspecialidad(EspecialidadRequest request) {
        Especialidad e = new Especialidad();
        e.setNombre(request.getNombre());
        e.setDescripcion(request.getDescripcion());
        return especialidadRepository.save(e);
    }

    public Especialidad updateEspecialidad(Long id, EspecialidadRequest request) {
        Especialidad existing = especialidadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Especialidad", id));
        existing.setNombre(request.getNombre());
        existing.setDescripcion(request.getDescripcion());
        return especialidadRepository.save(existing);
    }

    public void deleteEspecialidad(Long id) {
        if (!especialidadRepository.existsById(id)) {
            throw new ResourceNotFoundException("Especialidad", id);
        }
        especialidadRepository.deleteById(id);
    }
}
