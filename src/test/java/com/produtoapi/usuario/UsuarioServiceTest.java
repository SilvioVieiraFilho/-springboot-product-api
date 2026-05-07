package com.produtoapi.usuario;

import com.produtoapi.exception.BusinessException;
import com.produtoapi.usuario.domain.Usuario;
import com.produtoapi.usuario.dto.UsuarioRequestDTO;
import com.produtoapi.usuario.dto.UsuarioResponseDTO;
import com.produtoapi.usuario.enums.StatusUsuario;
import com.produtoapi.usuario.mapper.UsuarioMapper;
import com.produtoapi.usuario.repository.UsuarioRepository;
import com.produtoapi.usuario.service.UsuarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {
@Mock
UsuarioMapper mapper;
    @Mock
    private UsuarioRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Nested
    @DisplayName("Autenticação de usuário")
    class AutenticarUsuarioTest {

        @Test
        void deveAutenticarComSucesso() {

            Usuario usuario = new Usuario();
            usuario.setEmail("teste@email.com");
            usuario.setSenha("hash");
            usuario.setRole("USER");
            usuario.setStatus(StatusUsuario.ATIVO);

            UsuarioResponseDTO responseDTO = UsuarioResponseDTO.builder()
                    .email("teste@email.com")
                    .role("USER")
                    .build();

            when(repository.findByEmail("teste@email.com"))
                    .thenReturn(Optional.of(usuario));

            when(passwordEncoder.matches("123", "hash"))
                    .thenReturn(true);

            when(mapper.toResponse(usuario))
                    .thenReturn(responseDTO);

            UsuarioResponseDTO result =
                    usuarioService.autenticar("teste@email.com", "123");

            assertNotNull(result);
            assertEquals("teste@email.com", result.getEmail());
            assertEquals("USER", result.getRole());
        }

        @Test
        void deveLancarExcecao_QuandoEmailForNull() {

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> usuarioService.autenticar(null, "123")
            );

            assertEquals("Email e senha são obrigatórios", exception.getMessage());
        }

        @Test
        void deveLancarExcecao_QuandoEmailForBlank() {

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> usuarioService.autenticar("", "123")
            );

            assertEquals("Email e senha são obrigatórios", exception.getMessage());
        }

        @Test
        void deveLancarExcecao_QuandoSenhaForNull() {

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> usuarioService.autenticar("teste@email.com", null)
            );

            assertEquals("Email e senha são obrigatórios", exception.getMessage());
        }

        @Test
        void deveLancarExcecao_QuandoSenhaForBlank() {

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> usuarioService.autenticar("teste@email.com", "")
            );

            assertEquals("Email e senha são obrigatórios", exception.getMessage());
        }

        @Test
        void deveLancarExcecao_QuandoUsuarioNaoExistir() {

            when(repository.findByEmail("teste@email.com"))
                    .thenReturn(Optional.empty());

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> usuarioService.autenticar("teste@email.com", "123")
            );

            assertEquals("Credenciais inválidas", exception.getMessage());
        }

        @Test
        void deveLancarExcecao_QuandoSenhaForInvalida() {

            Usuario usuario = new Usuario();
            usuario.setEmail("teste@email.com");
            usuario.setSenha("hash");
            usuario.setStatus(StatusUsuario.ATIVO);

            when(repository.findByEmail("teste@email.com"))
                    .thenReturn(Optional.of(usuario));

            when(passwordEncoder.matches("123", "hash"))
                    .thenReturn(false);

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> usuarioService.autenticar("teste@email.com", "123")
            );

            assertEquals("Credenciais inválidas", exception.getMessage());
        }

        @Test
        void deveLancarExcecao_QuandoUsuarioEstiverInativo() {

            Usuario usuario = new Usuario();
            usuario.setEmail("teste@email.com");
            usuario.setSenha("hash");
            usuario.setStatus(StatusUsuario.DESATIVADO);

            when(repository.findByEmail("teste@email.com"))
                    .thenReturn(Optional.of(usuario));

            assertThrows(
                    BusinessException.class,
                    () -> usuarioService.autenticar("teste@email.com", "123")
            );
        }

        @Test
        void deveLancarExcecao_QuandoUsuarioEstiverBloqueado() {

            Usuario usuario = new Usuario();
            usuario.setEmail("teste@email.com");
            usuario.setSenha("hash");
            usuario.setStatus(StatusUsuario.BLOQUEADO);

            when(repository.findByEmail("teste@email.com"))
                    .thenReturn(Optional.of(usuario));

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> usuarioService.autenticar("teste@email.com", "123")
            );

            assertEquals("Usuário inativo ou bloqueado", exception.getMessage());

            verify(repository).findByEmail("teste@email.com");
            verifyNoInteractions(passwordEncoder);
            verifyNoInteractions(mapper);
        }

    }}