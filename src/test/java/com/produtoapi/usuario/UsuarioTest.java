package com.produtoapi.usuario;


import com.produtoapi.usuario.domain.Usuario;
import com.produtoapi.usuario.enums.StatusUsuario;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Nested
class UsuarioTest {

    @Nested
    class ValidarParaLogin {

        @Test
        void devePermitirLoginQuandoStatusAtivo() {
            Usuario usuario = new Usuario();
            usuario.setStatus(StatusUsuario.ATIVO);

            assertDoesNotThrow(usuario::validarParaLogin);
        }

        @Test
        void deveLancarExcecaoQuandoStatusForNull() {
            Usuario usuario = new Usuario();
            usuario.setStatus(null);

            RuntimeException ex = assertThrows(RuntimeException.class,
                    usuario::validarParaLogin);

            assertEquals("Usuário sem status definido", ex.getMessage());
        }
    }

    @Nested
    class AplicarDefaults {

        @Test
        void deveAplicarDefaultsQuandoCamposForemNull() {
            Usuario usuario = new Usuario();

            usuario.aplicarDefaults();

            assertEquals(StatusUsuario.ATIVO, usuario.getStatus());
            assertEquals("USER", usuario.getRole());
        }

        @Test
        void naoDeveSobrescreverValoresExistentes() {
            Usuario usuario = new Usuario();
            usuario.setStatus(StatusUsuario.ATIVO);
            usuario.setRole("ADMIN");

            usuario.aplicarDefaults();

            assertEquals(StatusUsuario.ATIVO, usuario.getStatus());
            assertEquals("ADMIN", usuario.getRole());
        }
    }
}