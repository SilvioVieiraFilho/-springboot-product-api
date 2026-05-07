package com.produtoapi.usuario.service;

import com.produtoapi.exception.BusinessException;
import com.produtoapi.usuario.dto.UsuarioRequestDTO;
import com.produtoapi.usuario.dto.UsuarioResponseDTO;
import com.produtoapi.usuario.enums.StatusUsuario;
import com.produtoapi.usuario.domain.Usuario;
import com.produtoapi.usuario.mapper.UsuarioMapper;
import com.produtoapi.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;




@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    private final UsuarioMapper mapper;
    public UsuarioResponseDTO autenticar(String email, String senha) {

        if (email == null || email.isBlank() || senha == null || senha.isBlank()) {
            throw new BusinessException("Email e senha são obrigatórios");
        }

        Usuario usuario = repository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Credenciais inválidas"));

        usuario.validarParaLogin();

        if (!passwordEncoder.matches(senha, usuario.getSenha())) {
            throw new BusinessException("Credenciais inválidas");
        }

        return mapper.toResponse(usuario);
    }




    public UsuarioResponseDTO salvar(UsuarioRequestDTO requestDTO) {

        // 1. validação
        if (requestDTO.getEmail() == null || requestDTO.getEmail().isBlank()) {
            throw new BusinessException("Email obrigatório");
        }

        if (requestDTO.getSenha() == null || requestDTO.getSenha().isBlank()) {
            throw new BusinessException("Senha obrigatória");
        }


        if (repository.existsByEmail(requestDTO.getEmail())) {
            throw new BusinessException("Email já cadastrado");
        }


        Usuario usuario = mapper.toEntity(requestDTO);

        usuario.aplicarDefaults();


        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));


        Usuario salvo = repository.save(usuario);


        return mapper.toResponse(salvo);
    }
}