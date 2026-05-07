package com.produtoapi.usuario;
import com.produtoapi.usuario.domain.Usuario;
import com.produtoapi.usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository repository;

    @Test
    void deveSalvarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setEmail("teste@email.com");
        usuario.setSenha("123");

        Usuario salvo = repository.save(usuario);

        assertNotNull(salvo.getId());
        assertEquals("teste@email.com", salvo.getEmail());
    }

    @Test
    void deveBuscarPorEmail() {
        Usuario usuario = new Usuario();
        usuario.setEmail("teste@email.com");
        usuario.setSenha("123");

        repository.save(usuario);

        Optional<Usuario> result = repository.findByEmail("teste@email.com");

        assertTrue(result.isPresent());
        assertEquals("teste@email.com", result.get().getEmail());
    }

    @Test
    void deveRetornarVazioQuandoNaoEncontrarEmail() {
        Optional<Usuario> result = repository.findByEmail("inexistente@email.com");

        assertTrue(result.isEmpty());
    }
}
