package com.produtoapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.produtoapi.security.controller.AuthController;
import com.produtoapi.security.filter.JwtAuthenticationFilter;
import com.produtoapi.security.service.JwtService;
import com.produtoapi.usuario.domain.Usuario;
import com.produtoapi.usuario.dto.UsuarioRequestDTO;
import com.produtoapi.usuario.dto.UsuarioResponseDTO;
import com.produtoapi.usuario.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest
        (AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Test
    void deveFazerLoginComSucesso() throws Exception {

        UsuarioResponseDTO responseDTO = UsuarioResponseDTO.builder()
                .email("teste@email.com")
                .role("USER")
                .build();

        Mockito.when(usuarioService.autenticar("teste@email.com", "123"))
                .thenReturn(responseDTO);

        Mockito.when(jwtService.generateToken("teste@email.com", "USER"))
                .thenReturn("token123");

        UsuarioRequestDTO request = UsuarioRequestDTO.builder()
                .email("teste@email.com")
                .senha("123")
                .build();

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Login realizado com sucesso"))
                .andExpect(jsonPath("$.data.token")
                        .value("token123"))
                .andExpect(jsonPath("$.data.email")
                        .value("teste@email.com"))
                .andExpect(jsonPath("$.data.role")
                        .value("USER"));
    }
    @Test
    void deveRegistrarUsuarioComSucesso() throws Exception {

        UsuarioResponseDTO responseDTO = UsuarioResponseDTO.builder()
                .email("novo@email.com")
                .role("USER")
                .build();

        Mockito.when(usuarioService.salvar(Mockito.any(UsuarioRequestDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "email": "novo@email.com",
                                "senha": "123"
                            }
                            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Usuário cadastrado com sucesso"));
    }
}
