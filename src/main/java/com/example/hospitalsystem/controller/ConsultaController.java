package com.example.hospitalsystem.controller;

import com.example.hospitalsystem.dto.common.ApiResponse;
import com.example.hospitalsystem.dto.consulta.ConsultaRequest;
import com.example.hospitalsystem.dto.consulta.ConsultaResponse;
import com.example.hospitalsystem.model.Diagnostico;
import com.example.hospitalsystem.service.ConsultaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultas")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ConsultaResponse>>> getAllConsultas() {
        return ResponseEntity.ok(ApiResponse.ok(consultaService.getAllConsultas()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ConsultaResponse>> getConsultaById(@PathVariable Long id) {
        return consultaService.getConsultaById(id)
                .map(c -> ResponseEntity.ok(ApiResponse.ok(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/paciente/{idPaciente}")
    public ResponseEntity<ApiResponse<List<ConsultaResponse>>> getConsultasByPaciente(@PathVariable Long idPaciente) {
        return ResponseEntity.ok(ApiResponse.ok(consultaService.getConsultasByPaciente(idPaciente)));
    }

    @GetMapping("/medico/{idMedico}")
    public ResponseEntity<ApiResponse<List<ConsultaResponse>>> getConsultasByMedico(@PathVariable Long idMedico) {
        return ResponseEntity.ok(ApiResponse.ok(consultaService.getConsultasByMedico(idMedico)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ConsultaResponse>> createConsulta(
            @Valid @RequestBody ConsultaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(consultaService.createConsulta(request), "Consulta registrada exitosamente"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ConsultaResponse>> updateConsulta(
            @PathVariable Long id, @Valid @RequestBody ConsultaRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(consultaService.updateConsulta(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsulta(@PathVariable Long id) {
        consultaService.deleteConsulta(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{idConsulta}/diagnosticos")
    public ResponseEntity<List<Diagnostico>> getDiagnosticosByConsulta(@PathVariable Long idConsulta) {
        return ResponseEntity.ok(consultaService.getDiagnosticosByConsulta(idConsulta));
    }

    @PostMapping("/{idConsulta}/diagnosticos")
    public ResponseEntity<Diagnostico> addDiagnostico(@PathVariable Long idConsulta,
                                                      @RequestBody Diagnostico diagnostico) {
        diagnostico.setIdConsulta(idConsulta);
        return ResponseEntity.status(HttpStatus.CREATED).body(consultaService.addDiagnostico(diagnostico));
    }
}
