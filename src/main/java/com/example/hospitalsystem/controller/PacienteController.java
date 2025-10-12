package com.example.hospitalsystem.controller;


import com.example.hospitalsystem.model.AntecedenteMedico;
import com.example.hospitalsystem.model.HistoriaClinica;
import com.example.hospitalsystem.model.Paciente;
import com.example.hospitalsystem.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
@CrossOrigin(origins = "http://localhost:3000")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @GetMapping
    public ResponseEntity<List<Paciente>> getAllPacientes() {
        return ResponseEntity.ok(pacienteService.getAllPacientes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Paciente> getPacienteById(@PathVariable Long id) {
        return pacienteService.getPacienteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<Paciente> getPacienteByDni(@PathVariable String dni) {
        return pacienteService.getPacienteByDni(dni)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Paciente> createPaciente(@RequestBody Paciente paciente) {
        Paciente savedPaciente = pacienteService.createPaciente(paciente);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPaciente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Paciente> updatePaciente(@PathVariable Long id, @RequestBody Paciente paciente) {
        Paciente updatedPaciente = pacienteService.updatePaciente(id, paciente);
        if (updatedPaciente != null) {
            return ResponseEntity.ok(updatedPaciente);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaciente(@PathVariable Long id) {
        if (pacienteService.deletePaciente(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/historia-clinica")
    public ResponseEntity<HistoriaClinica> getHistoriaClinica(@PathVariable Long id) {
        return pacienteService.getHistoriaClinicaByPaciente(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/historia/{idHistoria}/antecedentes")
    public ResponseEntity<List<AntecedenteMedico>> getAntecedentes(@PathVariable Long idHistoria) {
        return ResponseEntity.ok(pacienteService.getAntecedentesByHistoria(idHistoria));
    }

    @PostMapping("/historia/{idHistoria}/antecedentes")
    public ResponseEntity<AntecedenteMedico> addAntecedente(@PathVariable Long idHistoria,
                                                            @RequestBody AntecedenteMedico antecedente) {
        antecedente.setIdHistoria(idHistoria);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pacienteService.addAntecedenteMedico(antecedente));
    }
}
