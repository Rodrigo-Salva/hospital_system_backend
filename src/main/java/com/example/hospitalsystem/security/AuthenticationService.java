package com.example.hospitalsystem.security;

import com.example.hospitalsystem.dto.auth.AuthResponse;
import com.example.hospitalsystem.dto.auth.LoginRequest;
import com.example.hospitalsystem.dto.auth.RegisterRequest;
import com.example.hospitalsystem.exception.DuplicateResourceException;
import com.example.hospitalsystem.model.Rol;
import com.example.hospitalsystem.model.Usuario;
import com.example.hospitalsystem.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("El usuario '" + request.getUsername() + "' ya existe");
        }

        Set<Rol> roles = (request.getRoles() != null && !request.getRoles().isEmpty())
                ? request.getRoles()
                : Set.of(Rol.ROLE_RECEPCIONISTA);

        Usuario usuario = Usuario.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .activo(true)
                .build();

        usuarioRepository.save(usuario);
        String token = jwtService.generateToken(usuario);

        return buildAuthResponse(token, usuario);
    }

    public AuthResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String token = jwtService.generateToken(usuario);
        return buildAuthResponse(token, usuario);
    }

    private AuthResponse buildAuthResponse(String token, Usuario usuario) {
        Set<String> roleNames = usuario.getRoles().stream()
                .map(Rol::name)
                .collect(Collectors.toSet());

        return AuthResponse.builder()
                .token(token)
                .username(usuario.getUsername())
                .roles(roleNames)
                .build();
    }
}
