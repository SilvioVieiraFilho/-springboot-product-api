package com.produtoapi.dto;

import com.produtoapi.enums.ProdutoStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ProdutoResponseDTO {

	private Long id;
	private String nome;
	private Double preco;
	private int quantidade;
	private ProdutoStatus status;

}
