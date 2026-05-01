package com.produtoapi.domain;

import com.produtoapi.dto.ProdutoRequestDTO;
import com.produtoapi.enums.ProdutoStatus;
import com.produtoapi.exception.BusinessException;
import com.produtoapi.model.HistoricosProdutos;
import com.produtoapi.model.Produto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class ProdutoDomainService {

    public Produto atualizar(Produto produto, ProdutoRequestDTO dto) {

        int quantidadeAnterior = produto.getQuantidade();
        int novaQuantidade = quantidadeAnterior + dto.getQuantidade();

        produto.setQuantidade(novaQuantidade);

        produto.adicionarHistorico(
                criarHistorico(
                        quantidadeAnterior,
                        novaQuantidade,
                        dto.getQuantidade()
                )
        );

        aplicarRegraEstoque(produto);

        return produto;
    }

    public Produto inicializar(Produto produto) {

        produto.adicionarHistorico(
                criarHistorico(
                        0,
                        produto.getQuantidade(),
                        produto.getQuantidade()
                )
        );

        aplicarRegraEstoque(produto);

        return produto;
    }

    private HistoricosProdutos criarHistorico(int anterior, int nova, int diferenca) {

        HistoricosProdutos h = new HistoricosProdutos();
        h.setQuantidadeAnterior(anterior);
        h.setQuantidadeNova(nova);
        h.setDiferenca(diferenca);
        h.setDataRegistro(LocalDateTime.now());

        return h;
    }

    public void atualizarDadosBasicos(Produto produto, ProdutoRequestDTO dto) {

        if (dto.getNome() != null) {
            produto.setNome(dto.getNome());
        }

        if (dto.getPreco() != null) {
            produto.setPreco(dto.getPreco());
        }

        if (dto.getQuantidade() != null) {
            produto.setQuantidade(dto.getQuantidade());
        }

        if (dto.getStatus() != null) {
            produto.setStatus(dto.getStatus());
        }

        aplicarRegraEstoque(produto);
    }


    private void aplicarRegraEstoque(Produto produto) {
        produto.setStatus(produto.getQuantidade() == 0
                ? ProdutoStatus.ESGOTADO
                : ProdutoStatus.ATIVO);

    }

    public void validarStatusEsgotado(ProdutoRequestDTO dto) {

        if (dto.getStatus() == ProdutoStatus.ESGOTADO
                && dto.getQuantidade() > 0) {

            throw new BusinessException(
                    "Produto não pode estar ESGOTADO com quantidade maior que 0"
            );
        }
        if (dto.getStatus() == ProdutoStatus.ATIVO
                && dto.getQuantidade() == 0) {

            throw new BusinessException(
                    "Produto ATIVO não pode ter quantidade igual a 0"
            );
        }


    }
}
