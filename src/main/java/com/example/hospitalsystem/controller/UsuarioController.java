package com.example.hospitalsystem.controller;

import com.example.hospitalsystem.exception.ResourceNotFoundException;
import com.example.hospitalsystem.model.Rol;
import com.example.hospitalsystem.model.Usuario;
import com.example.hospitalsystem.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllUsuarios() {
        List<Map<String, Object>> usuarios = usuarioRepository.findAll().stream()
                .map(this::toMap)
                .collect(Collectors.toList());
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
        return ResponseEntity.ok(toMap(usuario));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Map<String, Object>> toggleEstado(@PathVariable Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
        usuario.setActivo(!usuario.isActivo());
        usuarioRepository.save(usuario);
        return ResponseEntity.ok(toMap(usuario));
    }

    @PutMapping("/{id}/roles")
    public ResponseEntity<Map<String, Object>> updateRoles(@PathVariable Long id,
                                                           @RequestBody Set<Rol> roles) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
        usuario.setRoles(roles);
        usuarioRepository.save(usuario);
        return ResponseEntity.ok(toMap(usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario", id);
        }
        usuarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Map<String, Object> toMap(Usuario u) {
        Set<String> roles = u.getRoles().stream().map(Rol::name).collect(Collectors.toSet());
        return Map.of(
                "id", u.getId(),
                "username", u.getUsername(),
                "roles", roles,
                "activo", u.isActivo()
        );
    }
}
