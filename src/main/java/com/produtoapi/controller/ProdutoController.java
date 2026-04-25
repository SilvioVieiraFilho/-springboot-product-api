package com.produtoapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.produtoapi.dto.ProdutoRequestDTO;
import com.produtoapi.dto.ProdutoResponseDTO;
import com.produtoapi.enums.ProdutoStatus;
import com.produtoapi.service.ProdutoService;

import jakarta.persistence.NamedQuery;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/produtos")
public class ProdutoController {

	@Autowired
	private ProdutoService serviceProduto;

	// LISTAR
	@GetMapping
	public List<ProdutoResponseDTO> listarTodos() {
		return serviceProduto.listarTodos();
	}

	// SALVAR
	@PostMapping
	public ProdutoResponseDTO salvarProduto(@RequestBody ProdutoRequestDTO dto) {
		return serviceProduto.salvar(dto);
	}

	// DELETAR
	@DeleteMapping("/{id}")
	public void deletarProduto(@PathVariable Long id) {
		serviceProduto.deletarProduto(id);
	}

	@GetMapping("/{id}")
	public ProdutoResponseDTO buscarProdutoPorId(@PathVariable Long id) {
		return serviceProduto.buscarPorId(id);
	}

	// ATUALIZAR
	@PutMapping("/{id}")
	public ProdutoResponseDTO atualizarProduto(@PathVariable Long id, @RequestBody ProdutoRequestDTO dto) {
		return serviceProduto.update(id, dto);
	}

	@PostMapping("/salvarLista")

	public List<ProdutoResponseDTO> salvarLista(@Valid @RequestBody List<ProdutoRequestDTO> dto) {

		return serviceProduto.salvarLista(dto);

	}

	@GetMapping("/filtro")

	public List<ProdutoResponseDTO> buscarFiltro(@RequestParam(required = false) String nome,@RequestParam(required = false) ProdutoStatus status,@RequestParam(required = false) Double preco) {

		return serviceProduto.buscarFiltro(nome,status,preco);
	}
}
