package com.produtoapi.factory;
import com.produtoapi.dto.ProdutoRequestDTO;
import com.produtoapi.model.Produto;
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