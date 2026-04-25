package com.produtoapi.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.produtoapi.dto.ProdutoRequestDTO;
import com.produtoapi.dto.ProdutoResponseDTO;
import com.produtoapi.model.Produto;

@Mapper(componentModel  = "spring")

public interface ProdutoMapper {

    Produto toEntity(ProdutoRequestDTO dto);

    ProdutoResponseDTO toDTO(Produto produto);

    List<Produto> toEntityList(List<ProdutoRequestDTO> dtoList);

    List<ProdutoResponseDTO> toDTOList(List<Produto> entityList);
}