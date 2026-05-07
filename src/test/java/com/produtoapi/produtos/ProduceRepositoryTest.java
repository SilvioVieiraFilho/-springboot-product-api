package com.produtoapi.produtos;

import com.produtoapi.produto.enums.ProdutoStatus;
import com.produtoapi.produto.domain.Produto;
import com.produtoapi.produto.repository.ProdutoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProdutoRepositoryTest {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Test
    @DisplayName("Deve salvar e buscar produto por nome")
    void deveBuscarProdutoPorNome() {

        Produto produto = new Produto();
        produto.setNome("Mouse");
        produto.setPreco(100.0);
        produto.setStatus(ProdutoStatus.ATIVO);

        produtoRepository.save(produto);

        List<Produto> resultado = produtoRepository.findByNome("Mouse");

        assertThat(resultado).isNotEmpty();
        assertThat(resultado.get(0).getNome()).isEqualTo("Mouse");
    }

    @Test
    @DisplayName("Deve verificar se produto existe por nome, preço e status")
    void deveVerificarExistenciaProduto() {

        Produto produto = new Produto();
        produto.setNome("Teclado");
        produto.setPreco(200.0);
        produto.setStatus(ProdutoStatus.ATIVO);

        produtoRepository.save(produto);

        boolean existe = produtoRepository.existsByNomeAndPrecoAndStatus(
                "Teclado", 200.0, ProdutoStatus.ATIVO
        );

        assertThat(existe).isTrue();
    }

    @Test
    @DisplayName("Deve buscar produto por nome, preço e status")
    void deveBuscarPorNomePrecoStatus() {

        Produto produto = new Produto();
        produto.setNome("Monitor");
        produto.setPreco(900.0);
        produto.setStatus(ProdutoStatus.ATIVO);

        produtoRepository.save(produto);

        Optional<Produto> resultado = produtoRepository
                .findByNomeAndPrecoAndStatus("Monitor", 900.0, ProdutoStatus.ATIVO);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNome()).isEqualTo("Monitor");
    }
}