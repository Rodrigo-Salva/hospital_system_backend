package com.example.hospitalsystem.controller;

import com.example.hospitalsystem.dto.common.ApiResponse;
import com.example.hospitalsystem.dto.paciente.PacienteRequest;
import com.example.hospitalsystem.dto.paciente.PacienteResponse;
import com.example.hospitalsystem.model.AntecedenteMedico;
import com.example.hospitalsystem.model.HistoriaClinica;
import com.example.hospitalsystem.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PacienteResponse>>> getAllPacientes(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String estado,
            @PageableDefault(size = 10, sort = "apellidos") Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(pacienteService.searchPacientes(search, estado, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PacienteResponse>> getPacienteById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(pacienteService.getPacienteById(id)));
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<ApiResponse<PacienteResponse>> getPacienteByDni(@PathVariable String dni) {
        return pacienteService.getPacienteByDni(dni)
                .map(p -> ResponseEntity.ok(ApiResponse.ok(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PacienteResponse>> createPaciente(
            @Valid @RequestBody PacienteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(pacienteService.createPaciente(request), "Paciente registrado exitosamente"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PacienteResponse>> updatePaciente(
            @PathVariable Long id, @Valid @RequestBody PacienteRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(
                pacienteService.updatePaciente(id, request), "Paciente actualizado exitosamente"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaciente(@PathVariable Long id) {
        pacienteService.deletePaciente(id);
        return ResponseEntity.noContent().build();
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
