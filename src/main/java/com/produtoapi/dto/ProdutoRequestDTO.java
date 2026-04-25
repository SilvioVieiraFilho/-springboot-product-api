package com.produtoapi.dto;

import com.produtoapi.enums.ProdutoStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoRequestDTO {

	private String nome;
	private Double preco;
	private int quantidade;
	private ProdutoStatus status;
	
	
}
