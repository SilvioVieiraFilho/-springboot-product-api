package com.produtoapi.produtos;

import com.produtoapi.dto.ProdutoRequestDTO;
import com.produtoapi.exception.BusinessException;
import com.produtoapi.exception.GlobalExceptionHandler;
import com.produtoapi.exception.ProdutoNotFoundException;
import jakarta.validation.Valid;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = GlobalExceptionHandlerTest.TestController.class)
@Import(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @RestController
    static class TestController {

        @GetMapping("/business-error")
        public String businessError() {
            throw new BusinessException("Erro de negócio");
        }

        @GetMapping("/generic-error")
        public String genericError() {
            throw new RuntimeException("Erro genérico");
        }

        @GetMapping("/not-found/{id}")
        public String notFound(@PathVariable Long id) {
            throw new ProdutoNotFoundException(id);
        }

        @PostMapping("/validacao")
        public String validar(@Valid @RequestBody ProdutoRequestDTO dto) {
            return "ok";
        }
    }

    @Nested
    class BusinessExceptionTest {

        @Test
        @DisplayName("Deve retornar 400 quando BusinessException for lançada")
        void deveRetornar400_quandoBusinessException() throws Exception {

            mockMvc.perform(get("/business-error"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
                    .andExpect(jsonPath("$.message").value("Erro de negócio"))
                    .andExpect(jsonPath("$.path").value("/business-error"));
        }
    }

    @Nested
    class GenericExceptionTest {

        @Test
        @DisplayName("Deve retornar 500 quando erro genérico ocorrer")
        void deveRetornar500_quandoErroGenerico() throws Exception {

            mockMvc.perform(get("/generic-error"))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.status").value(500))
                    .andExpect(jsonPath("$.error").value("INTERNAL_SERVER_ERROR"))
                    .andExpect(jsonPath("$.message").value("Erro inesperado no sistema"))
                    .andExpect(jsonPath("$.path").value("/generic-error"));
        }
    }

    @Nested
    class ProdutoNotFoundTest {

        @Test
        @DisplayName("Deve retornar 404 quando produto não for encontrado")
        void deveRetornar404_quandoProdutoNaoEncontrado() throws Exception {

            mockMvc.perform(get("/not-found/10"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(404))
                    .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.message")
                            .value("Produto não encontrado com ID: 10"))
                    .andExpect(jsonPath("$.path").value("/not-found/10"));
        }
    }

    @Nested
    class ValidationTest {

        @Test
        @DisplayName("Deve retornar 400 quando validação falhar")
        void deveRetornar400_quandoErroDeValidacao() throws Exception {

            String jsonInvalido = "{}";

            mockMvc.perform(post("/validacao")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonInvalido))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$").isArray());
        }
    }

    @Nested
    class NoHandlerFoundTest {

        @Test
        @DisplayName("Deve retornar 404 quando endpoint não existir")
        void deveRetornar404_quandoEndpointNaoExiste() throws Exception {

            mockMvc.perform(get("/rota-inexistente"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.message").value("Endpoint não encontrado"));
        }
    }
}