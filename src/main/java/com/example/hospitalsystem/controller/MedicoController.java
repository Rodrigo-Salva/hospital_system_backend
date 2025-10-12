package com.example.hospitalsystem.controller;

import com.example.hospitalsystem.model.Especialidad;
import com.example.hospitalsystem.model.Medico;
import com.example.hospitalsystem.service.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicos")
@CrossOrigin(origins = "http://localhost:3000")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    @GetMapping
    public ResponseEntity<List<Medico>> getAllMedicos() {
        return ResponseEntity.ok(medicoService.getAllMedicos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medico> getMedicoById(@PathVariable Long id) {
        return medicoService.getMedicoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Medico> createMedico(@RequestBody Medico medico) {
        return ResponseEntity.status(HttpStatus.CREATED).body(medicoService.createMedico(medico));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Medico> updateMedico(@PathVariable Long id, @RequestBody Medico medico) {
        Medico updatedMedico = medicoService.updateMedico(id, medico);
        if (updatedMedico != null) {
            return ResponseEntity.ok(updatedMedico);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedico(@PathVariable Long id) {
        if (medicoService.deleteMedico(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Endpoints para Especialidades
    @GetMapping("/especialidades")
    public ResponseEntity<List<Especialidad>> getAllEspecialidades() {
        return ResponseEntity.ok(medicoService.getAllEspecialidades());
    }

    @GetMapping("/especialidades/{id}")
    public ResponseEntity<Especialidad> getEspecialidadById(@PathVariable Long id) {
        return medicoService.getEspecialidadById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/especialidades")
    public ResponseEntity<Especialidad> createEspecialidad(@RequestBody Especialidad especialidad) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(medicoService.createEspecialidad(especialidad));
    }

    @PutMapping("/especialidades/{id}")
    public ResponseEntity<Especialidad> updateEspecialidad(@PathVariable Long id,
                                                           @RequestBody Especialidad especialidad) {
        Especialidad updated = medicoService.updateEspecialidad(id, especialidad);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/especialidades/{id}")
    public ResponseEntity<Void> deleteEspecialidad(@PathVariable Long id) {
        if (medicoService.deleteEspecialidad(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
