package com.produtoapi.produtos;


import com.produtoapi.dto.ProdutoRequestDTO;
import com.produtoapi.enums.ProdutoStatus;
import com.produtoapi.factory.ProdutoFactory;
import com.produtoapi.model.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProdutoFactoryTest {

    private ProdutoFactory factory;

    @BeforeEach
    void setup() {
        factory = new ProdutoFactory();
    }

    @Test
    void deveCriarProdutoAPartirDoDTO() {

        // Given
        ProdutoRequestDTO dto = ProdutoRequestDTO.builder()
                .nome("Produto A")
                .preco(10.0)
                .quantidade(5)
                .status(ProdutoStatus.ATIVO)
                .build();

        // When
        Produto produto = factory.criar(dto);

        // Then
        assertThat(produto).isNotNull();
        assertThat(produto.getNome()).isEqualTo("Produto A");
        assertThat(produto.getPreco()).isEqualTo(10.0);
        assertThat(produto.getQuantidade()).isEqualTo(5);
        assertThat(produto.getStatus()).isEqualTo(ProdutoStatus.ATIVO);
    }

    @Test
    void deveCriarProdutoComCamposNulos() {

        // Given
        ProdutoRequestDTO dto = ProdutoRequestDTO.builder().build();

        // When
        Produto produto = factory.criar(dto);

        // Then
        assertThat(produto).isNotNull();
        assertThat(produto.getNome()).isNull();
        assertThat(produto.getPreco()).isNull();
        assertThat(produto.getQuantidade()).isNull();
        assertThat(produto.getStatus()).isNull();
    }

}