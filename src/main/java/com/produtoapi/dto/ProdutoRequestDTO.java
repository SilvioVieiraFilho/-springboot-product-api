package com.produtoapi.dto;

import com.produtoapi.enums.ProdutoStatus;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.*;

@Getter
@Setter
public class ProdutoRequestDTO {

	@NotBlank(message = "Nome é obrigatório")
	private String nome;

	@NotNull(message = "Preço é obrigatório")
	@Positive(message = "Preço deve ser maior que zero")
	private Double preco;

	@NotNull(message = "Quantidade é obrigatória")
	@Min(value = 0, message = "Quantidade não pode ser negativa")
	private Integer quantidade;

	@NotNull(message = "Status é obrigatório")
	private ProdutoStatus status;

}
