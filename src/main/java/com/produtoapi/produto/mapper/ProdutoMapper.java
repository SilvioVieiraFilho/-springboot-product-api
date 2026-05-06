package com.produtoapi.produto.mapper;

import java.util.List;

import com.produtoapi.produto.domain.Produto;
import com.produtoapi.produto.dto.ProdutoResponseDTO;
import org.mapstruct.Mapper;

import com.produtoapi.produto.dto.ProdutoRequestDTO;

@Mapper(componentModel  = "spring")

public interface ProdutoMapper {

    Produto toEntity(ProdutoRequestDTO dto);

    ProdutoResponseDTO toDTO(Produto produto);

    List<Produto> toEntityList(List<ProdutoRequestDTO> dtoList);

    List<ProdutoResponseDTO> toDTOList(List<Produto> entityList);
}