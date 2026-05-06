package com.produtoapi.produto.domain;


import com.produtoapi.exception.BusinessException;
import org.springframework.stereotype.Service;

@Service
public class ProdutoDomainService {

    // =====================================================
    // 🔥 MOVIMENTAÇÃO DE ESTOQUE
    // =====================================================
    public void atualizar(Produto produto, int quantidade) {

        if (quantidade == 0) {
            throw new BusinessException("Quantidade não pode ser zero");
        }

        if (quantidade > 0) {
            produto.adicionarQuantidade(quantidade);
        } else {
            produto.removerQuantidade(Math.abs(quantidade));
        }
    }

    // =====================================================
    // 🔥 INICIALIZAÇÃO
    // =====================================================
    public void inicializar(Produto produto) {
        produto.definirQuantidade(produto.getQuantidade());
    }

    // =====================================================
    // 🔥 DADOS BÁSICOS
    // =====================================================
    public void atualizarDadosBasicos(Produto produto,
                                      String nome,
                                      Double preco,
                                      Integer quantidade) {

        if (nome != null) {
            produto.setNome(nome);
        }

        if (preco != null) {
            produto.setPreco(preco);
        }

        if (quantidade != null) {
            produto.definirQuantidade(quantidade);
        }
    }
}