package com.produtoapi.security.controller;


import com.produtoapi.usuario.dto.UsuarioRequestDTO;
import com.produtoapi.usuario.dto.UsuarioResponseDTO;
import com.produtoapi.usuario.domain.Usuario;

import com.produtoapi.usuario.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.produtoapi.security.service.JwtService;
import lombok.RequiredArgsConstructor;
import response.ApiResponse;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> login(@RequestBody UsuarioRequestDTO request) {

        UsuarioResponseDTO usuario = usuarioService.autenticar(
                request.getEmail(),
                request.getSenha()
        );

        String token = jwtService.generateToken(
                usuario.getEmail(),
                usuario.getRole()
        );

        UsuarioResponseDTO response = UsuarioResponseDTO.builder()
                .token(token)
                .type("Bearer")
                .email(usuario.getEmail())
                .role(usuario.getRole())
                .build();


        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Login realizado com sucesso",
                        response
                ));

    }


    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@RequestBody UsuarioRequestDTO usuarioRequestDTO) {

        usuarioService.salvar(usuarioRequestDTO);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Usuário cadastrado com sucesso",
                        "OK"
                )
        );
    }
}