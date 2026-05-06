package com.produtoapi.produto.domain;
import com.produtoapi.produto.domain.Produto;
import com.produtoapi.produto.dto.ProdutoRequestDTO;
import org.springframework.stereotype.Component;



@Component
public class ProdutoFactory {

    public Produto criar(ProdutoRequestDTO dto) {
        return Produto.builder()
                .nome(dto.getNome())
                .preco(dto.getPreco())
                .quantidade(dto.getQuantidade())
                .status(dto.getStatus())
                .build();
    }
}