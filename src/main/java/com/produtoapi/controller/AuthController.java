package com.produtoapi.controller;


import com.produtoapi.dto.UsuarioRequestDTO;
import com.produtoapi.dto.UsuarioResponseDTO;
import com.produtoapi.model.Usuario;

import com.produtoapi.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.produtoapi.security.JwtService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<UsuarioResponseDTO> login(@RequestBody UsuarioRequestDTO request) {

        Usuario usuario = usuarioService.autenticar(
                request.getEmail(),
                request.getSenha()
        );

        String token = jwtService.generateToken(
                usuario.getEmail(),
                usuario.getRole()
        );

        return ResponseEntity.ok(
                UsuarioResponseDTO.builder()
                        .token(token)
                        .type("Bearer")
                        .email(usuario.getEmail())
                        .role(usuario.getRole())
                        .build());
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Usuario usuario) {

        usuarioService.salvar(usuario);

        return ResponseEntity.ok("Usuário cadastrado com sucesso");
    }
}