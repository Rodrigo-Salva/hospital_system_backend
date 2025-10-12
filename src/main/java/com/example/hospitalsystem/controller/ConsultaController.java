package com.example.hospitalsystem.controller;

import com.example.hospitalsystem.model.Consulta;
import com.example.hospitalsystem.model.Diagnostico;
import com.example.hospitalsystem.service.ConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultas")
@CrossOrigin(origins = "http://localhost:3000")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    @GetMapping
    public ResponseEntity<List<Consulta>> getAllConsultas() {
        return ResponseEntity.ok(consultaService.getAllConsultas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Consulta> getConsultaById(@PathVariable Long id) {
        return consultaService.getConsultaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/paciente/{idPaciente}")
    public ResponseEntity<List<Consulta>> getConsultasByPaciente(@PathVariable Long idPaciente) {
        return ResponseEntity.ok(consultaService.getConsultasByPaciente(idPaciente));
    }

    @GetMapping("/medico/{idMedico}")
    public ResponseEntity<List<Consulta>> getConsultasByMedico(@PathVariable Long idMedico) {
        return ResponseEntity.ok(consultaService.getConsultasByMedico(idMedico));
    }

    @PostMapping
    public ResponseEntity<Consulta> createConsulta(@RequestBody Consulta consulta) {
        return ResponseEntity.status(HttpStatus.CREATED).body(consultaService.createConsulta(consulta));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Consulta> updateConsulta(@PathVariable Long id, @RequestBody Consulta consulta) {
        Consulta updatedConsulta = consultaService.updateConsulta(id, consulta);
        if (updatedConsulta != null) {
            return ResponseEntity.ok(updatedConsulta);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsulta(@PathVariable Long id) {
        if (consultaService.deleteConsulta(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Endpoints para Diagnósticos
    @GetMapping("/{idConsulta}/diagnosticos")
    public ResponseEntity<List<Diagnostico>> getDiagnosticosByConsulta(@PathVariable Long idConsulta) {
        return ResponseEntity.ok(consultaService.getDiagnosticosByConsulta(idConsulta));
    }

    @PostMapping("/{idConsulta}/diagnosticos")
    public ResponseEntity<Diagnostico> addDiagnostico(@PathVariable Long idConsulta,
                                                      @RequestBody Diagnostico diagnostico) {
        diagnostico.setIdConsulta(idConsulta);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(consultaService.addDiagnostico(diagnostico));
    }
}

