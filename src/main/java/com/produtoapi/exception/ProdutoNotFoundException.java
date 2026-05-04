package com.produtoapi.exception;

public class ProdutoNotFoundException extends RuntimeException {

	public ProdutoNotFoundException(Long id) {

		super("Produto não encontrado com ID: " + id);

	}

}
