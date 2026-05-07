package com.produtoapi.security;


import ch.qos.logback.core.status.Status;
import com.produtoapi.security.service.JwtService;
import com.produtoapi.usuario.domain.Usuario;
import com.produtoapi.usuario.enums.StatusUsuario;
import com.produtoapi.usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class JwtAuthenticationFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @Test
    void deveAutenticarComTokenValido() throws Exception {

        String token = "valid-token";

        Usuario usuario = Mockito.mock(Usuario.class);
        StatusUsuario status = Mockito.mock(StatusUsuario.class);

        Mockito.when(jwtService.extractUsername(token))
                .thenReturn("user@email.com");

        Mockito.when(jwtService.isTokenValid(token, "user@email.com"))
                .thenReturn(true);

        Mockito.when(usuarioRepository.findByEmail("user@email.com"))
                .thenReturn(Optional.of(usuario));

        Mockito.when(usuario.getEmail()).thenReturn("user@email.com");
        Mockito.when(usuario.getRole()).thenReturn("USER");
        Mockito.when(usuario.getStatus()).thenReturn(status);
        Mockito.doNothing().when(status).validarLogin();

        mockMvc.perform(get("/test")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void deveNegarComTokenInvalido() throws Exception {

        String token = "invalid-token";

        Mockito.when(jwtService.extractUsername(token))
                .thenThrow(new RuntimeException("Token inválido"));

        mockMvc.perform(get("/test")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }
}
