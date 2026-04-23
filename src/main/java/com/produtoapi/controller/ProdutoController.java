package com.produtoapi.controller;

import java.lang.foreign.Linker.Option;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.produtoapi.model.Produto;
import com.produtoapi.service.ProdutoService;

@RestController
@RequestMapping("/produto")

public class ProdutoController {

	@Autowired
	ProdutoService serviceProduto;

	@GetMapping
	public List<Produto> listarTodos() {

		return serviceProduto.listarTodos();

	}

	@PostMapping

	public Produto salvarProduto(@RequestBody Produto produto) {

		return serviceProduto.salvar(produto);
	}

	@DeleteMapping("/{id}")
	public void deletarProduto(@PathVariable Long id) {

		serviceProduto.deletarProduto(id);

	}

	@PutMapping("/{id}")

	public Produto updataProduto(@PathVariable Long id, @RequestBody Produto produto) {

		return serviceProduto.update(id, produto);

	}

	@GetMapping("/{id}")

	public Optional<Produto> buscarProdutoPorId(@PathVariable Long id) {

		return serviceProduto.encontraumid(id);

	}

}
