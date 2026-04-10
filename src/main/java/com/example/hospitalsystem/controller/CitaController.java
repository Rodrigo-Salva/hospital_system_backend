package com.example.hospitalsystem.controller;

import com.example.hospitalsystem.dto.cita.CitaRequest;
import com.example.hospitalsystem.dto.cita.CitaResponse;
import com.example.hospitalsystem.dto.common.ApiResponse;
import com.example.hospitalsystem.service.CitaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    @Autowired
    private CitaService citaService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CitaResponse>>> getAllCitas(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @PageableDefault(size = 10, sort = "fecha") Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(citaService.searchCitas(estado, fecha, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CitaResponse>> getCitaById(@PathVariable Long id) {
        return citaService.getCitaById(id)
                .map(c -> ResponseEntity.ok(ApiResponse.ok(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/paciente/{idPaciente}")
    public ResponseEntity<ApiResponse<List<CitaResponse>>> getCitasByPaciente(@PathVariable Long idPaciente) {
        return ResponseEntity.ok(ApiResponse.ok(citaService.getCitasByPaciente(idPaciente)));
    }

    @GetMapping("/medico/{idMedico}")
    public ResponseEntity<ApiResponse<List<CitaResponse>>> getCitasByMedico(@PathVariable Long idMedico) {
        return ResponseEntity.ok(ApiResponse.ok(citaService.getCitasByMedico(idMedico)));
    }

    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<ApiResponse<List<CitaResponse>>> getCitasByFecha(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(ApiResponse.ok(citaService.getCitasByFecha(fecha)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CitaResponse>> createCita(@Valid @RequestBody CitaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(citaService.createCita(request), "Cita registrada exitosamente"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CitaResponse>> updateCita(
            @PathVariable Long id, @Valid @RequestBody CitaRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(citaService.updateCita(id, request)));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<CitaResponse>> cambiarEstado(@PathVariable Long id,
                                                                   @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(ApiResponse.ok(citaService.cambiarEstado(id, body.get("estado"))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCita(@PathVariable Long id) {
        citaService.deleteCita(id);
        return ResponseEntity.noContent().build();
    }
}
