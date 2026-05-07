package com.produtoapi.produtos;

import com.produtoapi.produto.enums.ProdutoStatus;
import com.produtoapi.exception.BusinessException;
import com.produtoapi.produto.domain.Produto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class ProdutoDomainTest {

    @Test
    void deveAdicionarQuantidadeEAtualizarStatus() {

        Produto produto = Produto.builder()
                .quantidade(5)
                .status(ProdutoStatus.ATIVO)
                .build();

        produto.adicionarQuantidade(3);

        assertThat(produto.getQuantidade()).isEqualTo(8);
        assertThat(produto.getStatus()).isEqualTo(ProdutoStatus.ATIVO);
        assertThat(produto.getHistoricos()).hasSize(1);
    }

    @Test
    void deveLancarExcecaoAoAdicionarQuantidadeZeroOuNegativa() {

        Produto produto = Produto.builder()
                .quantidade(5)
                .build();

        assertThatThrownBy(() -> produto.adicionarQuantidade(0))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("maior que zero");

        assertThatThrownBy(() -> produto.adicionarQuantidade(-1))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    void deveRemoverQuantidadeComSucesso() {

        Produto produto = Produto.builder()
                .quantidade(10)
                .status(ProdutoStatus.ATIVO)
                .build();

        produto.removerQuantidade(4);

        assertThat(produto.getQuantidade()).isEqualTo(6);
        assertThat(produto.getStatus()).isEqualTo(ProdutoStatus.ATIVO);
        assertThat(produto.getHistoricos()).hasSize(1);
    }

    @Test
    void deveMarcarComoEsgotadoQuandoZerarEstoque() {

        Produto produto = Produto.builder()
                .quantidade(5)
                .status(ProdutoStatus.ATIVO)
                .build();

        produto.removerQuantidade(5);

        assertThat(produto.getQuantidade()).isZero();
        assertThat(produto.getStatus()).isEqualTo(ProdutoStatus.ESGOTADO);
    }

    @Test
    void deveLancarExcecaoQuandoEstoqueFicarNegativo() {

        Produto produto = Produto.builder()
                .quantidade(3)
                .build();

        assertThatThrownBy(() -> produto.removerQuantidade(10))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("negativo");
    }


    @Test
    void deveDefinirQuantidadeComSucesso() {

        Produto produto = Produto.builder()
                .quantidade(5)
                .status(ProdutoStatus.ATIVO)
                .build();

        produto.definirQuantidade(10);

        assertThat(produto.getQuantidade()).isEqualTo(10);
        assertThat(produto.getHistoricos()).hasSize(1);
    }

    @Test
    void deveLancarExcecaoAoDefinirQuantidadeNegativa() {

        Produto produto = Produto.builder()
                .quantidade(5)
                .build();

        assertThatThrownBy(() -> produto.definirQuantidade(-1))
                .isInstanceOf(BusinessException.class);
    }


    @Test
    void deveMarcarComoEsgotadoQuandoQuantidadeZero() {

        Produto produto = Produto.builder()
                .quantidade(1)
                .status(ProdutoStatus.ATIVO)
                .build();

        produto.removerQuantidade(1);

        assertThat(produto.getStatus()).isEqualTo(ProdutoStatus.ESGOTADO);
    }
}

