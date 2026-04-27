package com.produtoapi.exception;

public class ProdutoNotFoundExcepetion extends RuntimeException {

	public ProdutoNotFoundExcepetion(Long id) {

		super("Produto não encontrado com ID: " + id);

	}

}
