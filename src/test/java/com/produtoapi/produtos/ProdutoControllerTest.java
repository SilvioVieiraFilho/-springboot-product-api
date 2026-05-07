package com.produtoapi.produtos;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.produtoapi.produto.controller.ProdutoController;
import com.produtoapi.produto.dto.ProdutoRequestDTO;
import com.produtoapi.produto.dto.ProdutoResponseDTO;
import com.produtoapi.produto.enums.ProdutoStatus;
import com.produtoapi.produto.service.ProdutoService;
import com.produtoapi.security.filter.JwtAuthenticationFilter;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(ProdutoController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProdutoService serviceProduto;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Nested
    class ListarProdutos {

        @Test
        @WithMockUser
        void deveListarTodos() throws Exception {

            ProdutoResponseDTO dto = new ProdutoResponseDTO();
            dto.setId(1L);
            dto.setNome("Mouse");

            when(serviceProduto.listarTodos()).thenReturn(List.of(dto));

            mockMvc.perform(get("/produtos"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data[0].nome").value("Mouse"));
        }
    }

    @Nested
    class SalvarProduto {

        @Test
        @WithMockUser
        void deveSalvarProduto() throws Exception {

            ProdutoRequestDTO request = new ProdutoRequestDTO();
            request.setNome("Teclado");
            request.setPreco(100.0);
            request.setQuantidade(10);
            request.setStatus(ProdutoStatus.ATIVO);

            ProdutoResponseDTO response = new ProdutoResponseDTO();
            response.setId(1L);
            response.setNome("Teclado");

            when(serviceProduto.salvar(any())).thenReturn(response);

            mockMvc.perform(post("/produtos")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.data.nome").value("Teclado"));
        }
    }

    @Nested
    class BuscarProduto {

        @Test
        @WithMockUser
        void deveBuscarPorId() throws Exception {

            ProdutoResponseDTO response = new ProdutoResponseDTO();
            response.setId(1L);
            response.setNome("Mouse");

            when(serviceProduto.buscarPorId(1L)).thenReturn(response);

            mockMvc.perform(get("/produtos/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.nome").value("Mouse"));
        }
    }

    @Nested
    class DeletarProduto {

        @Test
        @WithMockUser
        void deveDeletarProduto() throws Exception {

            doNothing().when(serviceProduto).deletarProduto(1L);

            mockMvc.perform(delete("/produtos/1")
                            .with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("Produto deletado com sucesso"));
        }
    }

    @Nested
    class AtualizarProduto {

        @Test
        @WithMockUser
        void deveAtualizarProduto() throws Exception {

            ProdutoRequestDTO request = new ProdutoRequestDTO();
            request.setNome("Mouse");
            request.setPreco(100.0);
            request.setQuantidade(10);
            request.setStatus(ProdutoStatus.ATIVO);

            ProdutoResponseDTO response = new ProdutoResponseDTO();
            response.setId(1L);
            response.setNome("Mouse");

            when(serviceProduto.atualizarProduto(eq(1L), any())).thenReturn(response);

            mockMvc.perform(put("/produtos/1")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.nome").value("Mouse"));
        }
    }

    @Nested
    class SalvarLista {
        @Test
        @WithMockUser
        void deveSalvarLista() throws Exception {

            ProdutoRequestDTO request = new ProdutoRequestDTO();
            request.setNome("Teclado");
            request.setPreco(100.0);
            request.setQuantidade(10);
            request.setStatus(ProdutoStatus.ATIVO);

            ProdutoResponseDTO response = new ProdutoResponseDTO();
            response.setId(1L);
            response.setNome("Teclado");

            when(serviceProduto.salvarLista(anyList()))
                    .thenReturn(List.of(response));

            mockMvc.perform(post("/produtos/salvarLista")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(List.of(request))))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.data[0].nome").value("Teclado"));
        }

        @Nested
        class FiltroProdutos {

            @Test
            @WithMockUser
            void deveFiltrarProdutos() throws Exception {

                ProdutoResponseDTO response = new ProdutoResponseDTO();
                response.setId(1L);
                response.setNome("Mouse");
                when(serviceProduto.buscarFiltro(
                        nullable(String.class),
                        any(),
                        any(),
                        any()
                )).thenReturn(List.of(response));

                mockMvc.perform(get("/produtos/filtro"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.data[0].nome").value("Mouse"));
            }
        }
    }
}