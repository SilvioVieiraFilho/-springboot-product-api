package com.produtoapi.produtos;

import com.produtoapi.controller.ProdutoController;
import com.produtoapi.dto.ProdutoRequestDTO;
import com.produtoapi.dto.ProdutoResponseDTO;
import com.produtoapi.enums.ProdutoStatus;
import com.produtoapi.service.ProdutoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProdutoController.class)
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProdutoService serviceProduto;

    @Autowired
    private ObjectMapper objectMapper;

    // LISTAR
    @Test
    void deveListarProdutos() throws Exception {

        ProdutoResponseDTO produto = new ProdutoResponseDTO();
        produto.setId(1L);
        produto.setNome("Mouse");

        Mockito.when(serviceProduto.listarTodos())
                .thenReturn(List.of(produto));

        mockMvc.perform(get("/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].nome").value("Mouse"));
    }

    @Test
    void deveSalvarProduto() throws Exception {

        ProdutoRequestDTO request = new ProdutoRequestDTO();
        request.setNome("Teclado");
        request.setPreco(100.0);
        request.setQuantidade(10);
        request.setStatus(ProdutoStatus.ATIVO);

        ProdutoResponseDTO response = new ProdutoResponseDTO();
        response.setId(1L);
        response.setNome("Teclado");

        Mockito.when(serviceProduto.salvar(Mockito.any(ProdutoRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Produto cadastrado com sucesso"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.nome").value("Teclado"));
    }



    // BUSCAR POR ID
    @Test
    void deveBuscarPorId() throws Exception {

        ProdutoResponseDTO response = new ProdutoResponseDTO();
        response.setId(1L);
        response.setNome("Mouse");

        Mockito.when(serviceProduto.buscarPorId(1L))
                .thenReturn(response);

        mockMvc.perform(get("/produtos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nome").value("Mouse"));
    }

    // DELETAR
    @Test
    void deveDeletarProduto() throws Exception {

        Mockito.doNothing().when(serviceProduto).deletarProduto(1L);

        mockMvc.perform(delete("/produtos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Produto deletado com sucesso"));
    }
}