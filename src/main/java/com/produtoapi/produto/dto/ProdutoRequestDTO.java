package com.produtoapi.produto.dto;

import com.produtoapi.enums.ProdutoStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoRequestDTO {

	@NotBlank(message = "Nome é obrigatório")
	private String nome;

	@NotNull(message = "Preço é obrigatório")
	@PositiveOrZero(message = "Preço deve ser maior que zero")
	private Double preco;

	@NotNull(message = "Quantidade é obrigatória")
	@PositiveOrZero
	@Min(value = 0, message = "Quantidade não pode ser negativa")
	private Integer quantidade;

	@NotNull(message = "Status é obrigatório")
	private ProdutoStatus status;

}
