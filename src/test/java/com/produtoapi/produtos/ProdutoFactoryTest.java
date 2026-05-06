package com.produtoapi.produtos;


import com.produtoapi.produto.dto.ProdutoRequestDTO;
import com.produtoapi.enums.ProdutoStatus;
import com.produtoapi.produto.domain.ProdutoFactory;
import com.produtoapi.produto.domain.Produto;
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


        ProdutoRequestDTO dto = ProdutoRequestDTO.builder()
                .nome("Produto A")
                .preco(10.0)
                .quantidade(5)
                .status(ProdutoStatus.ATIVO)
                .build();


        Produto produto = factory.criar(dto);


        assertThat(produto).isNotNull();
        assertThat(produto.getNome()).isEqualTo("Produto A");
        assertThat(produto.getPreco()).isEqualTo(10.0);
        assertThat(produto.getQuantidade()).isEqualTo(5);
        assertThat(produto.getStatus()).isEqualTo(ProdutoStatus.ATIVO);
    }


    @Test
    void deveCriarProdutoComQuantidadeZero() {

        ProdutoRequestDTO dto = ProdutoRequestDTO.builder()
                .nome("Teclado")
                .preco(50.0)
                .quantidade(0)
                .status(ProdutoStatus.ESGOTADO)
                .build();

        Produto produto = factory.criar(dto);

        assertThat(produto.getQuantidade()).isZero();
        assertThat(produto.getStatus()).isEqualTo(ProdutoStatus.ESGOTADO);
    }

}