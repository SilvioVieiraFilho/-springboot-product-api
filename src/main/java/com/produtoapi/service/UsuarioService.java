package com.produtoapi.service;

import com.produtoapi.enums.StatusUsuario;
import com.produtoapi.model.Usuario;
import com.produtoapi.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;




@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;


    private void validarUsuario(Usuario usuario) {
        if (usuario.getStatus() == null) {
            throw new RuntimeException("Usuário sem status definido");
        }
        usuario.getStatus().validarLogin();
    }
    public Usuario autenticar(String email, String senha) {




        Usuario usuario = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));


        validarUsuario(usuario);


        if (!passwordEncoder.matches(senha, usuario.getSenha())) {
            throw new RuntimeException("Senha inválida");
        }

        return usuario;
    }

    public Usuario salvar(Usuario usuario) {

        if (usuario.getStatus() == null) {
            usuario.setStatus(StatusUsuario.ATIVO);
        }
        if (usuario.getRole() == null) {
            usuario.setRole("USER");
        }

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        return repository.save(usuario);
    }
}