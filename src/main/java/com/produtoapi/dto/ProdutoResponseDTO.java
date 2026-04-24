package com.produtoapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ProdutoResponseDTO {

	private Long id;
	private String nome;
	private Double preco;
	private int quantidade;
	private String status;

}
