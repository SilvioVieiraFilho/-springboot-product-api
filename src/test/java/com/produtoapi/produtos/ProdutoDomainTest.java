package com.produtoapi.produtos;

import com.produtoapi.domain.ProdutoDomainService;
import com.produtoapi.dto.ProdutoRequestDTO;
import com.produtoapi.enums.ProdutoStatus;
import com.produtoapi.exception.BusinessException;
import com.produtoapi.model.HistoricosProdutos;
import com.produtoapi.model.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ProdutoDomainTest {

    private ProdutoDomainService domain;

    @BeforeEach
    void setup() {
        domain = new ProdutoDomainService();
    }

    @Nested
    class AtualizarProduto {

        @Test
        void deveAtualizarQuantidadeECriarHistorico() {

            Produto produto = Produto.builder()
                    .nome("Peteca")
                    .preco(20.0)
                    .quantidade(5)
                    .status(ProdutoStatus.ATIVO)
                    .build();

            ProdutoRequestDTO dto = ProdutoRequestDTO.builder()
                    .quantidade(3)
                    .build();

            domain.atualizar(produto, dto);

            assertThat(produto.getQuantidade()).isEqualTo(8);
            assertThat(produto.getHistoricos()).hasSize(1);

            HistoricosProdutos historicoInicial = produto.getHistoricos().get(0);

            assertThat(historicoInicial.getQuantidadeAnterior()).isEqualTo(5);
            assertThat(historicoInicial.getQuantidadeNova()).isEqualTo(8);
            assertThat(historicoInicial.getDiferenca()).isEqualTo(3);

            assertThat(produto.getStatus()).isEqualTo(ProdutoStatus.ATIVO);
        }
    }

    @Nested
    class InicializarProduto {

        @Test
        void deveCriarHistoricoInicial() {

            Produto produto = Produto.builder()
                    .nome("Peteca")
                    .preco(20.0)
                    .quantidade(5)
                    .status(ProdutoStatus.ATIVO)
                    .build();

            domain.inicializar(produto);

            assertThat(produto.getHistoricos()).hasSize(1);

            HistoricosProdutos historicoInicial = produto.getHistoricos().get(0);

            assertThat(historicoInicial.getQuantidadeAnterior()).isEqualTo(0);
            assertThat(historicoInicial.getQuantidadeNova()).isEqualTo(5);
        }

        @Test
        void deveMarcarComoEsgotadoQuandoQuantidadeZero() {

            Produto produto = Produto.builder()
                    .nome("Peteca")
                    .preco(50.0)
                    .quantidade(0)
                    .status(ProdutoStatus.ESGOTADO)
                    .build();

            domain.inicializar(produto);

            assertThat(produto.getStatus()).isEqualTo(ProdutoStatus.ESGOTADO);
        }
    }

    @Nested
    class AtualizarDadosBasicos {

        @Test
        void deveAtualizarSomenteCamposPreenchidos() {

            Produto produto = Produto.builder()
                    .nome("Nome antigo")
                    .preco(10.0)
                    .quantidade(4)
                    .status(ProdutoStatus.ATIVO)
                    .build();

            ProdutoRequestDTO dto = new ProdutoRequestDTO();
            dto.setNome("Novo Nome");

            domain.atualizarDadosBasicos(produto, dto);

            assertThat(produto.getNome()).isEqualTo("Novo Nome");
            assertThat(produto.getPreco()).isEqualTo(10.0);
            assertThat(produto.getStatus()).isEqualTo(ProdutoStatus.ATIVO);
            assertThat(produto.getQuantidade()).isEqualTo(4);
        }
    }

    @Nested
    class Validacoes {

        @Test
        void deveLancarExcecaoQuandoStatusEsgotadoComQuantidadeMaiorQueZero() {

            ProdutoRequestDTO dto = new ProdutoRequestDTO();
            dto.setStatus(ProdutoStatus.ESGOTADO);
            dto.setQuantidade(5);

            assertThatThrownBy(() -> domain.validarStatusEsgotado(dto))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("ESGOTADO");
        }
    }
}