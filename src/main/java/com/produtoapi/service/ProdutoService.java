package com.produtoapi.service;

import java.util.List;
import java.util.Optional;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.produtoapi.model.*;
import com.produtoapi.repository.ProdutoRepository;

@Service
public class ProdutoService {

	@Autowired
	ProdutoRepository produtorepository;

	public List<Produto> listarTodos() {

		return produtorepository.findAll();

	}

	public Produto salvar(Produto produto) {

		return produtorepository.save(produto);

	}

	public void deletarProduto(Long id) {

		produtorepository.deleteById(id);

	}

	public Produto update(Long id, Produto produto) {

		Optional<Produto> produtoExistente = produtorepository.findById(id);

		if (produtoExistente.isPresent()) {

			Produto p = produtoExistente.get();

			p.setNome(produto.getNome());
			p.setQuantidade(produto.getQuantidade());
			p.setPreco(produto.getPreco());
			p.setStatus(produto.getStatus());

			return produtorepository.save(p);
		} else {
			throw new RuntimeException("produto não encontrado");
		}

	}

	public Optional<Produto> encontraumid(Long id) {

		return produtorepository.findById(id);

	}

}
