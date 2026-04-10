package com.example.hospitalsystem.controller;

import com.example.hospitalsystem.dto.common.ApiResponse;
import com.example.hospitalsystem.dto.medico.EspecialidadRequest;
import com.example.hospitalsystem.dto.medico.MedicoRequest;
import com.example.hospitalsystem.dto.medico.MedicoResponse;
import com.example.hospitalsystem.model.Especialidad;
import com.example.hospitalsystem.service.MedicoService;
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
@RequestMapping("/api/medicos")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<MedicoResponse>>> getAllMedicos(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String estado,
            @PageableDefault(size = 10, sort = "apellidos") Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(medicoService.searchMedicos(search, estado, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MedicoResponse>> getMedicoById(@PathVariable Long id) {
        return medicoService.getMedicoById(id)
                .map(m -> ResponseEntity.ok(ApiResponse.ok(m)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MedicoResponse>> createMedico(
            @Valid @RequestBody MedicoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(medicoService.createMedico(request), "Médico registrado exitosamente"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MedicoResponse>> updateMedico(
            @PathVariable Long id, @Valid @RequestBody MedicoRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(
                medicoService.updateMedico(id, request), "Médico actualizado exitosamente"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedico(@PathVariable Long id) {
        medicoService.deleteMedico(id);
        return ResponseEntity.noContent().build();
    }

    // ---- Especialidades ----

    @GetMapping("/especialidades")
    public ResponseEntity<ApiResponse<List<Especialidad>>> getAllEspecialidades() {
        return ResponseEntity.ok(ApiResponse.ok(medicoService.getAllEspecialidades()));
    }

    @GetMapping("/especialidades/{id}")
    public ResponseEntity<Especialidad> getEspecialidadById(@PathVariable Long id) {
        return medicoService.getEspecialidadById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/especialidades")
    public ResponseEntity<ApiResponse<Especialidad>> createEspecialidad(
            @Valid @RequestBody EspecialidadRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(medicoService.createEspecialidad(request), "Especialidad creada exitosamente"));
    }

    @PutMapping("/especialidades/{id}")
    public ResponseEntity<ApiResponse<Especialidad>> updateEspecialidad(
            @PathVariable Long id, @Valid @RequestBody EspecialidadRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(medicoService.updateEspecialidad(id, request)));
    }

    @DeleteMapping("/especialidades/{id}")
    public ResponseEntity<Void> deleteEspecialidad(@PathVariable Long id) {
        medicoService.deleteEspecialidad(id);
        return ResponseEntity.noContent().build();
    }
}
